package com.spring.emprendedoresApp.models.validation;

import com.spring.emprendedoresApp.models.dtos.ResponseDTO;
import com.spring.emprendedoresApp.persistence.entities.UserEntity;

public class UserValidation {

	public ResponseDTO validate(UserEntity user) {

		ResponseDTO response = new ResponseDTO();
		response.setNumOfError(0);

		if (user.getUsername() == null || user.getUsername().length() < 2 || user.getUsername().length() > 20) {

			response.setNumOfError(response.getNumOfError() + 1);
			response.setMessage("El nombre no puede ser nulo y debe tener entre 2 y 20 caracteres.");
		}

		if (user.getEmail() == null || !user.getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")) {

			response.setNumOfError(response.getNumOfError() + 1);
			response.setMessage("El campo email no es válido.");
		}

		if (user.getPassword() == null
				|| !user.getPassword().matches("^(?=.*[A-Z])(?=.*[0-9])(?=.*[@#$%^&+=]).{8,16}$")) {

			response.setNumOfError(response.getNumOfError() + 1);
			response.setMessage(
					"La contraseña debe tener entre 8 y 16 caracteres, incluir una letra mayúscula, un número y un símbolo especial.");
		}

		if (user.getPhone() == null || !user.getPhone().matches("^\\+?[0-9]{7,15}$")) {

			response.setNumOfError(response.getNumOfError() + 1);
			response.setMessage(
					"El teléfono no es válido. Debe contener entre 7 y 15 dígitos, y puede incluir un prefijo internacional.");
		}

		if (user.getCity() == null || user.getCity().length() < 2 || user.getCity().length() > 20) {

			response.setNumOfError(response.getNumOfError() + 1);
			response.setMessage("La Ciudad no puede ser nulo y debe tener entre 2 y 20 caracteres.");
		}

		if (user.getCountry() == null || user.getCountry().length() < 2 || user.getCountry().length() > 20) {

			response.setNumOfError(response.getNumOfError() + 1);
			response.setMessage("El Pais no puede ser nulo y debe tener entre 2 y 20 caracteres.");
		}

		return response;
	}
}
