package com.example.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.dto.ActualizacionAsesoriaDTO;
import com.example.dto.NuevaAsesoriaDTO;
import com.example.service.EmailService;

@RestController
@RequestMapping("/api/notificaciones")
public class EmailController {
	private final EmailService emailService;
	
	public EmailController(final EmailService emailService) {
		this.emailService = emailService;
	}
	
	@PostMapping("/nueva-asesoria")
	public ResponseEntity<String> notificarNuevaAsesoria(@RequestBody NuevaAsesoriaDTO dto) {
		emailService.sendNuevaAsesoriaNotification(dto);
		return ResponseEntity.ok("Notificación de nueva asesoría enviada a " + dto.getProgramadorEmail());
	}

	@PostMapping("/estado-asesoria")
	public ResponseEntity<String> notificarActualizacionAsesoria(@RequestBody ActualizacionAsesoriaDTO dto) {
		emailService.sendActualizacionAsesoriaNotification(dto);
		return ResponseEntity.ok("Notificación de actualización de estado enviada a " + dto.getSolicitanteEmail());
	}
}
