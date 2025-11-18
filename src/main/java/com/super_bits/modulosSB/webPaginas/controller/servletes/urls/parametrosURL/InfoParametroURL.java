/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.super_bits.modulosSB.webPaginas.controller.servletes.urls.parametrosURL;

import com.super_bits.modulosSB.SBCore.modulos.Controller.Interfaces.TIPO_PARTE_URL;
import com.super_bits.modulosSB.SBCore.modulos.fabrica.FabObjetosAbstratos;
import com.super_bits.modulosSB.SBCore.modulos.fabrica.ComoFabrica;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author Salvio
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface InfoParametroURL {

    public String nome();

    public String valorPadrao() default "";

    public TIPO_PARTE_URL tipoParametro();

    public Class tipoEntidade() default void.class;

    public boolean obrigatorio() default true;

    public boolean representaEntidadePrincipalMB() default false;

    public Class<? extends ComoFabrica> fabricaObjetosRelacionada() default FabObjetosAbstratos.class;

}
