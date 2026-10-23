package com.proyectolp2.HotelMorfeo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.servlet.http.*;
import lombok.RequiredArgsConstructor;
import com.proyectolp2.HotelMorfeo.dto.UsuarioSesion;
import com.proyectolp2.HotelMorfeo.service.BoletaService;
import com.proyectolp2.HotelMorfeo.util.Alert;

@Controller
@RequestMapping("boleta")
@RequiredArgsConstructor
public class BoletaController {
	private final BoletaService boletaService;

	@GetMapping("listado")
	public String listado(Model model, HttpServletRequest request, HttpServletResponse httpResponse) {
		httpResponse.setHeader("Cache-Control", "no-store");
		var sesion = request.getSession(false);
		if (sesion == null || !(sesion.getAttribute("usuarioSesion") instanceof UsuarioSesion usuario))
			return "redirect:/login?sesion";
		if (usuario.idUsuario() == null || usuario.idUsuario() <= 0
				|| (!Integer.valueOf(2).equals(usuario.idRol()) && !Integer.valueOf(3).equals(usuario.idRol()))) {
			sesion.invalidate();
			return "redirect:/login?sesion";
		}

		model.addAttribute("lstBoletas", boletaService.getAll());
		return "boleta/listado";
	}

	@GetMapping("nuevo")
	public String nuevo(@RequestParam(required = false) Integer idReserva, Model model, HttpServletRequest request,
			HttpServletResponse httpResponse) {
		httpResponse.setHeader("Cache-Control", "no-store");
		var sesion = request.getSession(false);
		if (sesion == null || !(sesion.getAttribute("usuarioSesion") instanceof UsuarioSesion usuario))
			return "redirect:/login?sesion";
		if (usuario.idUsuario() == null || usuario.idUsuario() <= 0
				|| (!Integer.valueOf(2).equals(usuario.idRol()) && !Integer.valueOf(3).equals(usuario.idRol()))) {
			sesion.invalidate();
			return "redirect:/login?sesion";
		}

		model.addAttribute("lstReservas", boletaService.getReservasActivas());
		model.addAttribute("idReserva", idReserva);
		if (idReserva != null) {
			try {
				model.addAttribute("boleta", boletaService.preparar(idReserva));
			} catch (IllegalArgumentException e) {
				model.addAttribute("error", e.getMessage());
			}
		}
		return "boleta/nuevo";
	}

	@PostMapping("registrar")
	public String registrar(@RequestParam Integer idReserva, RedirectAttributes flash, HttpServletRequest request,
			HttpServletResponse httpResponse) {
		httpResponse.setHeader("Cache-Control", "no-store");
		var sesion = request.getSession(false);
		if (sesion == null || !(sesion.getAttribute("usuarioSesion") instanceof UsuarioSesion usuario))
			return "redirect:/login?sesion";
		if (usuario.idUsuario() == null || usuario.idUsuario() <= 0
				|| (!Integer.valueOf(2).equals(usuario.idRol()) && !Integer.valueOf(3).equals(usuario.idRol()))) {
			sesion.invalidate();
			return "redirect:/login?sesion";
		}

		try {
			Integer id = boletaService.create(idReserva);
			flash.addFlashAttribute("alert", Alert.sweetToast("Boleta emitida correctamente.", "success", 5000));
			return "redirect:/boleta/ver/" + id;
		} catch (IllegalArgumentException e) {
			flash.addFlashAttribute("error", e.getMessage());
			return "redirect:/boleta/nuevo?idReserva=" + idReserva;
		}
	}

	@GetMapping("ver/{id}")
	public String ver(@PathVariable Integer id, Model model, RedirectAttributes flash, HttpServletRequest request,
			HttpServletResponse httpResponse) {
		httpResponse.setHeader("Cache-Control", "no-store");
		var sesion = request.getSession(false);
		if (sesion == null || !(sesion.getAttribute("usuarioSesion") instanceof UsuarioSesion usuario))
			return "redirect:/login?sesion";
		if (usuario.idUsuario() == null || usuario.idUsuario() <= 0
				|| (!Integer.valueOf(2).equals(usuario.idRol()) && !Integer.valueOf(3).equals(usuario.idRol()))) {
			sesion.invalidate();
			return "redirect:/login?sesion";
		}

		var boleta = boletaService.getOne(id);
		if (boleta == null) {
			flash.addFlashAttribute("alert", Alert.sweetToast("La boleta no existe.", "error", 5000));
			return "redirect:/boleta/listado";
		}
		model.addAttribute("boleta", boleta);
		return "boleta/ver";
	}
}
