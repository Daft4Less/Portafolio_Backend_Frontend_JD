package ec.edu.ups.ppw.portafolio.model.dto;

public class TelegramPayload {
    private long chat_id;
    private String mensaje;

    public TelegramPayload() {
    }

    public TelegramPayload(long chat_id, String mensaje) {
        this.chat_id = chat_id;
        this.mensaje = mensaje;
    }

    public long getChat_id() {
        return chat_id;
    }

    public void setChat_id(long chat_id) {
        this.chat_id = chat_id;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}

