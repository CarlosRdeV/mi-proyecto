package com.taskmanager.gamified.entity;

public enum DificultadTarea {
    FACIL(10),
    MEDIA(25),
    DIFICIL(50),
    ESPECIAL(0); // XP personalizable
    
    private final int xpBase;
    
    DificultadTarea(int xpBase) {
        this.xpBase = xpBase;
    }
    
    public int getXpBase() {
        return xpBase;
    }
}