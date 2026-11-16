CREATE TABLE IF NOT EXISTS public.usuario (
    id UUID PRIMARY KEY,
    primer_nombre VARCHAR(100),
    primer_apellido VARCHAR(100),
    usuario VARCHAR(100) UNIQUE,
    password VARCHAR(255),
    correo VARCHAR(150),
    ruta_foto VARCHAR(255)
);

INSERT INTO public.usuario (id, primer_nombre, primer_apellido, usuario, password, correo, ruta_foto)
VALUES (gen_random_uuid(), 'Admin', 'Sistema', 'admin', '1234', 'admin@uam.edu.ni', '')
ON CONFLICT (usuario) DO NOTHING;
