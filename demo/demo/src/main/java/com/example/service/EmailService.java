package com.example.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.example.dto.ActualizacionAsesoriaDTO;
import com.example.dto.NuevaAsesoriaDTO;

@Service
public class EmailService {
	
	private final JavaMailSender emailSender;
	
	public EmailService(final JavaMailSender emailSender) {
		this.emailSender = emailSender;
	}

	/**
	 * Notifica al programador sobre una nueva solicitud de asesoría.
	 */
	public void sendNuevaAsesoriaNotification(NuevaAsesoriaDTO dto) {
		String subject = "Nueva Solicitud de Asesoría de: " + dto.getSolicitanteNombre();
		
		StringBuilder body = new StringBuilder();
		body.append("Has recibido una nueva solicitud de asesoría.\n\n");
		body.append("De: ").append(dto.getSolicitanteNombre()).append("\n");
		body.append("Motivo: ").append(dto.getComentario()).append("\n\n");
		body.append("Puedes gestionar esta solicitud en el portal de la aplicación.");

		this.sendEmail(dto.getProgramadorEmail(), subject, body.toString());
	}

	/**
	 * Notifica al usuario solicitante sobre un cambio de estado en su asesoría.
	 */
	public void sendActualizacionAsesoriaNotification(ActualizacionAsesoriaDTO dto) {
		String subject = "Actualización sobre tu Asesoría: " + dto.getNuevoEstado();
		
		StringBuilder body = new StringBuilder();
		body.append("Hola, ").append(dto.getSolicitanteNombre()).append(".\n\n");
		body.append("Tu solicitud de asesoría con el motivo \"").append(dto.getComentario()).append("\" ha sido actualizada.\n");
		body.append("Nuevo estado: ").append(dto.getNuevoEstado().toUpperCase()).append("\n\n");
		body.append("Puedes ver más detalles en el portal de la aplicación.");

		this.sendEmail(dto.getSolicitanteEmail(), subject, body.toString());
	}
	
	/**
	 * Método auxiliar privado para enviar el correo.
	 */
	private void sendEmail (String to, String subject, String content) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(to);
		message.setSubject(subject);
		message.setText(content);
		emailSender.send(message);
	}
}
