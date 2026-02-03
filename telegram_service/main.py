import asyncio
import os
import httpx
from fastapi import FastAPI
from pydantic import BaseModel
from dotenv import load_dotenv

load_dotenv()

app = FastAPI()

TELEGRAM_BOT_TOKEN = os.getenv("TELEGRAM_BOT_TOKEN")
TELEGRAM_API_URL = f"https://api.telegram.org/bot{TELEGRAM_BOT_TOKEN}"

class MensajeCliente(BaseModel):
    chat_id: int
    mensaje: str

async def send_telegram_message(chat_id: int, text: str):
    payload = {"chat_id": chat_id, "text": text}
    async with httpx.AsyncClient() as client:
        try:
            response = await client.post(f"{TELEGRAM_API_URL}/sendMessage", json=payload)
            return response.json()
        except Exception as e:
            print(f"Error al enviar: {e}")

async def process_update(update):
    if "message" in update:
        chat_id = update["message"]["chat"]["id"]
        user = update["message"]["from"].get("first_name", "Usuario")
        text = update["message"].get("text")
        
        # ESTO ES LO QUE VERÁS EN TU CONSOLA DE VS CODE
        print(f"--- NUEVO MENSAJE ---")
        print(f"Usuario: {user}")
        print(f"CHAT_ID: {chat_id}")
        print(f"Texto: {text}")
        print(f"---------------------")

        if text:
            await send_telegram_message(chat_id, f"Has dicho: {text}")

async def telegram_polling():
    offset = 0
    print(">>> BOT INICIADO EN MODO POLLING <<<")
    async with httpx.AsyncClient(timeout=30.0) as client:
        while True:
            try:
                params = {"offset": offset, "timeout": 20}
                response = await client.get(f"{TELEGRAM_API_URL}/getUpdates", params=params)
                updates = response.json().get("result", [])
                for update in updates:
                    await process_update(update)
                    offset = update["update_id"] + 1
            except Exception as e:
                await asyncio.sleep(5)

@app.on_event("startup")
async def startup_event():
    asyncio.create_task(telegram_polling())

# ESTE ES EL ENDPOINT QUE BUSCABAS
@app.post("/enviar-cliente")
async def enviar_a_cliente(data: MensajeCliente):
    resultado = await send_telegram_message(data.chat_id, data.mensaje)
    return {"status": "Enviado", "telegram_data": resultado}

@app.get("/")
async def root():
    return {"status": "Online"}