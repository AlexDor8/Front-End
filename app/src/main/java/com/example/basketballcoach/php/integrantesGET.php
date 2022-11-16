<?php
$integrantes = [
    ["nombreApellidos" => "Alejandro Dorado Casado", "especializacion" => "Front End", "informacionIntegrante" => "Gran pasión por la programación \ny por el arte digital.", "foto" => ""],
    ["nombreApellidos" => "Kilian Herrada Fernández", "especializacion" => "Back End", "informacionIntegrante" => "Interés por el código abierto y por \nmejorar como programador.", "foto" => ""],
    ["nombreApellidos" => "Tigé David Ral Ramirez", "especializacion" => "Informe técnico", "informacionIntegrante" => "Me gusta expresar mi creatividad \na través del desarrollo de " +
    "software.\nY me satisface resolver problemas\nlógicos.", "foto" => ""]
];
echo json_encode($integrantes);