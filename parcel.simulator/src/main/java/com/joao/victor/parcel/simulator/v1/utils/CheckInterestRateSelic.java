package com.joao.victor.parcel.simulator.v1.utils;

import com.google.gson.Gson;
import com.joao.victor.parcel.simulator.v1.dtos.InterestRateResponse;
import lombok.Data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public @Data class CheckInterestRateSelic implements Serializable {

    static String webService = "https://api.bcb.gov.br/dados/serie/bcdata.sgs.11/dados?formato=";

    private BigDecimal interestRate;

    private String date;

    private String value;

    public static BigDecimal check () throws Exception {
        InterestRateResponse res = fetchInterestRate();
        return BigDecimal.valueOf(Double.parseDouble(res.getValor()));
    }

    public static InterestRateResponse fetchInterestRate() throws Exception {

        LocalDate localDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedString = localDate.format(formatter);

        String urlParaChamada = webService + "json&" +
                "dataInicial=" + formattedString +
                "&dataFinal=" + formattedString;

        try {
            URL url = new URL(urlParaChamada);
            HttpURLConnection conexao = (HttpURLConnection) url.openConnection();

            if (conexao.getResponseCode() != 200)
                throw new RuntimeException("HTTP error code : " + conexao.getResponseCode());

            BufferedReader resposta = new BufferedReader(new InputStreamReader((conexao.getInputStream())));
            String jsonEmString = convertJsonString(resposta);
            jsonEmString = jsonEmString.replace("[", "").replace("]", "");
            Gson gson = new Gson();
            InterestRateResponse r = gson.fromJson(jsonEmString, InterestRateResponse.class);

            return r;
        } catch (Exception e) {
            throw new Exception("ERRO: " + e);
        }
    }

        public static String convertJsonString(BufferedReader buffereReader) throws IOException {
            String resposta, jsonString = "";
            while ((resposta = buffereReader.readLine()) != null) {
                jsonString += resposta;
            }
            return jsonString;
        }
}
