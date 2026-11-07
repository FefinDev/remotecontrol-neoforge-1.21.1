# RemoteControl (NeoForge 1.21.1)

Mod de utilidad: un **Remote Control** que guarda la posición de un **Control Block** y lo enciende/apaga a distancia (redstone 15 en todas las direcciones).

- Minecraft 1.21.1 · NeoForge 21.1.x · Java 21 · Mod ID `remotecontrol` · Autor: Fefin

## Uso
- **Shift + clic derecho sobre un bloque**: guarda sus coordenadas (sonido de orbe de XP).
- **Clic derecho normal** (aire o bloque): alterna el Control Block guardado.

## Compilar
Requiere JDK 21 y conexión a internet (primera vez).

```bash
./gradlew build          # Windows: gradlew.bat build
```
El jar queda en `build/libs/remotecontrol-1.0.0.jar`.

Probar en el juego: `./gradlew runClient`.

## Instalar
1. Instala Minecraft 1.21.1 con NeoForge 21.1.x.
2. Copia `remotecontrol-1.0.0.jar` a la carpeta `mods/`.

## Texturas
Se regeneran con `python3 tools/generate_textures.py` (requiere Pillow). Rutas:

| Archivo | Ruta |
|---|---|
| Control Block apagado | `src/main/resources/assets/remotecontrol/textures/block/control_block_off.png` |
| Control Block encendido | `src/main/resources/assets/remotecontrol/textures/block/control_block_on.png` |
| Remote Control | `src/main/resources/assets/remotecontrol/textures/item/remote_control.png` |
