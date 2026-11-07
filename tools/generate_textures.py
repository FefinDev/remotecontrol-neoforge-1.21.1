#!/usr/bin/env python3
"""Genera las 3 texturas 16x16 de RemoteControl de forma reproducible.

Uso (desde la raiz del proyecto):  python3 tools/generate_textures.py
Requiere Pillow (pip install pillow).
"""
from pathlib import Path
from PIL import Image

ROOT = Path(__file__).resolve().parent.parent
TEX = ROOT / "src/main/resources/assets/remotecontrol/textures"

def hexc(h, a=255):
    h = h.lstrip("#")
    return (int(h[0:2], 16), int(h[2:4], 16), int(h[4:6], 16), a)

def shade(c, d):
    return tuple(max(0, min(255, v + d)) for v in c[:3]) + (255,)

def noise(x, y):
    # Ruido determinista (sin random) para que ambas texturas compartan la misma base.
    return ((x * 7 + y * 13 + x * y * 3) % 5) - 2

# --- Paleta ---------------------------------------------------------------
BASE      = hexc("#3b3f46")
LIGHT     = hexc("#5b616b")
DARK      = hexc("#202328")
RIVET     = hexc("#8d939e")
RING      = hexc("#15181c")
TRACE_OFF = hexc("#282c32")
TRACE_ON  = hexc("#2f86b8")
PANEL_OFF = hexc("#1f262e")
BTN_OFF   = hexc("#2b343f")
PANEL_ON_EDGE   = hexc("#1f8fd6")
PANEL_ON_MID    = hexc("#52c8ff")
PANEL_ON_CENTER = hexc("#e0f8ff")

def control_block(on):
    img = Image.new("RGBA", (16, 16))
    px = img.load()
    for y in range(16):
        for x in range(16):
            c = shade(BASE, noise(x, y) * 3)
            # Borde biselado: arriba/izquierda claro, abajo/derecha oscuro.
            if y == 0 or x == 0:
                c = LIGHT
            if y == 15 or x == 15:
                c = DARK
            px[x, y] = c
    px[0, 15] = DARK
    px[15, 0] = LIGHT

    # Remaches en las esquinas.
    for (x, y) in [(2, 2), (13, 2), (2, 13), (13, 13)]:
        px[x, y] = RIVET

    # Pistas de circuito alrededor del panel (mismo trazado en ON y OFF).
    trace = TRACE_ON if on else TRACE_OFF
    for i in range(5, 11):
        px[i, 2] = trace
        px[i, 13] = trace
        px[2, i] = trace
        px[13, i] = trace

    # Marco del panel central (8x8, de 4 a 11).
    for i in range(4, 12):
        px[i, 4] = RING
        px[i, 11] = RING
        px[4, i] = RING
        px[11, i] = RING

    # Interior del panel (6x6, de 5 a 10).
    for y in range(5, 11):
        for x in range(5, 11):
            if on:
                edge = x in (5, 10) or y in (5, 10)
                px[x, y] = PANEL_ON_EDGE if edge else PANEL_ON_MID
            else:
                edge = x in (5, 10) or y in (5, 10)
                px[x, y] = PANEL_OFF if edge else shade(PANEL_OFF, 6)
    # Nucleo 2x2.
    for (x, y) in [(7, 7), (8, 7), (7, 8), (8, 8)]:
        px[x, y] = PANEL_ON_CENTER if on else BTN_OFF
    return img

def remote_control():
    img = Image.new("RGBA", (16, 16), (0, 0, 0, 0))
    px = img.load()
    OUTLINE = hexc("#0f1114")
    BODY    = hexc("#2e3238")
    HI      = hexc("#4a505a")
    SH      = hexc("#1d2024")
    GRAY    = hexc("#8d939e")
    BLUE    = hexc("#3db8ff")
    BLUE_HI = hexc("#c4f1ff")
    SCREEN  = hexc("#0f2230")

    # Antena.
    px[10, 1] = GRAY
    px[10, 2] = GRAY
    px[10, 0] = BLUE

    # Cuerpo: contorno x 4..11, y 3..15.
    for y in range(3, 16):
        for x in range(4, 12):
            edge = x in (4, 11) or y in (3, 15)
            if edge:
                px[x, y] = OUTLINE
            else:
                c = BODY
                if x == 5 or y == 4:
                    c = HI
                if x == 10 or y == 14:
                    c = SH
                px[x, y] = c

    # Pantalla 4x2.
    for y in (5, 6):
        for x in range(6, 10):
            px[x, y] = SCREEN
    px[6, 5] = hexc("#2f9fd8")
    px[7, 5] = hexc("#2f9fd8")

    # Boton azul central (anillo 4x4 + nucleo 2x2).
    for y in range(8, 12):
        for x in range(6, 10):
            px[x, y] = RING
    for (x, y) in [(7, 9), (8, 9), (7, 10), (8, 10)]:
        px[x, y] = BLUE
    px[7, 9] = BLUE_HI

    # Botones pequenos.
    px[6, 13] = GRAY
    px[9, 13] = GRAY
    px[7, 13] = SH
    px[8, 13] = SH
    return img

def main():
    (TEX / "block").mkdir(parents=True, exist_ok=True)
    (TEX / "item").mkdir(parents=True, exist_ok=True)
    control_block(False).save(TEX / "block/control_block_off.png")
    control_block(True).save(TEX / "block/control_block_on.png")
    remote_control().save(TEX / "item/remote_control.png")
    print("Texturas generadas en", TEX)

if __name__ == "__main__":
    main()
