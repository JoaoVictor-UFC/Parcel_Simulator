package com.joao.victor.parcel.simulator.v1.security;

import com.joao.victor.parcel.simulator.errorExceptions.MessageErrorCustom;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.joao.victor.parcel.simulator.v1.dtos.LoginRequest;
import com.joao.victor.parcel.simulator.v1.dtos.UserResponse;
import com.joao.victor.parcel.simulator.v1.service.SaleService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final SaleService saleService;
    private final Environment environment;

    public AuthenticationFilter(SaleService saleService, Environment environment,
                                AuthenticationManager authenticationManager) {
        this.saleService = saleService;
        this.environment = environment;
        super.setAuthenticationManager(authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res)
            throws AuthenticationException {
        try {
            LoginRequest creds = new ObjectMapper().readValue(req.getInputStream(), LoginRequest.class);
            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(creds.getLogin(), creds.getPassword(), new ArrayList<>()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain,
                                            Authentication auth) throws IOException, ServletException {

        String login = ((User) auth.getPrincipal()).getUsername();
        UserResponse user = saleService.fromUserEntityToUserResponse(saleService.findUserByLogin(login));
        ObjectMapper objectMapper = new ObjectMapper();
        Long expirationTime = Long.parseLong(environment.getProperty("token.expiration_time"));
        String token = Jwts.builder().setSubject(user.getLogin())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(SignatureAlgorithm.HS512, environment.getProperty("token.secret")).compact();
        res.addHeader("token", token);
        res.setStatus(200);
        res.addHeader("Content-Type", "application/json");
        res.addHeader("expiration_time", new Date(System.currentTimeMillis() + expirationTime).toString());
        res.getWriter().write(objectMapper.writeValueAsString(user));
    }


    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest req, HttpServletResponse res,
                                              AuthenticationException failed) throws IOException, ServletException {

        final String data = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm:ss"));
        final MessageErrorCustom custom = new MessageErrorCustom();
        Gson gson = new Gson();
        custom.setDatahora(data);
        custom.setError("Forbidden");
        custom.setMensagem("Error logging in.Please check your credentials.");
        custom.setStatus(HttpStatus.BAD_REQUEST.value());
        custom.setPath(req.getRequestURI());
        res.setStatus(400);
        res.addHeader("Content-Type", "application/json");
        res.getWriter().write(gson.toJson(custom));

    }

}
