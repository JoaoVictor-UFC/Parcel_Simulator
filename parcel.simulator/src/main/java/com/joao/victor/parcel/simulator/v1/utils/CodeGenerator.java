package com.joao.victor.parcel.simulator.v1.utils;

import lombok.Data;

import java.util.Random;

public @Data class CodeGenerator {

    private static Random random = new Random();

    public static Integer gerandoCodigo4Digitos() {

        int primeiro = 1000;
        int segundo = 9999;
        int result = random.nextInt(segundo - primeiro) + primeiro;
        return result;
    }

    public static Integer gerandoCodigo6Digitos() {

        int primeiro = 100000;
        int segundo = 999999;
        int result = random.nextInt(segundo - primeiro) + primeiro;
        return result;
    }
}
