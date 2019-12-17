/*
 *  Desenvolvido pela equipe Super-Bits.com CNPJ 20.019.971/0001-90

 */
package com.super_bits.modulosSB.webPaginas.controller.servletes.urls;

import com.super_bits.modulosSB.SBCore.modulos.Controller.Interfaces.ItfParametroRequisicaoInstanciado;
import com.super_bits.modulosSB.SBCore.modulos.Controller.Interfaces.TIPO_PARTE_URL;

import com.super_bits.modulosSB.webPaginas.controller.servletes.urls.parametrosURL.InfoParametroURL;
import com.super_bits.modulosSB.webPaginas.controller.servletes.urls.parametrosURL.ParametroUrlInstanciado;
import javax.persistence.EntityManager;

/**
 *
 * @author desenvolvedor
 */
public class ParteURLServlet implements ItfParametroRequisicaoInstanciado {

    private final ParametroUrlInstanciado parametroURL;

    public ParteURLServlet(ParametroUrlInstanciado pParametroURL) {
        parametroURL = pParametroURL;
    }

    public ParteURLServlet(InfoParametroURL pInfo) {
        parametroURL = new ParametroUrlInstanciado(pInfo);
    }

    @Override
    public String getNome() {
        return parametroURL.getNome();
    }

    @Override
    public Class getTipoEntidade() {
        return parametroURL.getTipoEntidade();
    }

    @Override
    public TIPO_PARTE_URL getTipoParametro() {
        return parametroURL.getTipoParametro();
    }

    @Override
    public Object getValor() {
        return parametroURL.getValor();
    }

    @Override
    public Object getValorPadrao() {
        return parametroURL.getValorPadrao();
    }

    @Override
    public void aplicarParteURLenviada(String pParteEnviada, EntityManager pEm) throws Throwable {
        parametroURL.aplicarParteURLenviada(pParteEnviada, pEm);
    }

    @Override
    public boolean isParametroObrigatorio() {
        return parametroURL.isParametroObrigatorio();
    }

    public ParametroUrlInstanciado getComoParametroURL() {
        return parametroURL;
    }

    @Override
    public void setValor(Object valor) {
        parametroURL.setValor(valor);
    }

    @Override
    public boolean isValorDoParametroFoiConfigurado() {
        return parametroURL.isValorDoParametroFoiConfigurado();
    }

    @Override
    public boolean isUmParametroDeEntidade() {
        return parametroURL.isUmParametroDeEntidade();
    }

    @Override
    public boolean isUmParametoEntidadeMBPrincipal() {
        return parametroURL.isUmParametoEntidadeMBPrincipal();
    }

    @Override
    public String getSlugValorParametro() {
        return parametroURL.getSlugValorParametro();
    }

    @Override
    public boolean isPossuiFabricaDeObjetos() {
        return parametroURL.isPossuiFabricaDeObjetos();
    }

    @Override
    public Class getClasseObjetoValor() {
        return parametroURL.getClasseObjetoValor();
    }

    @Override
    public String getSlugValorPadrao() {
        return parametroURL.getSlugValorPadrao();
    }

    @Override
    public Object getObjetoPorNomeFabrica(String pValor) {
        return parametroURL.getObjetoPorNomeFabrica(pValor);
    }

    @Override
    public String getTextoEnviadoUrl() {
        return parametroURL.getTextoEnviadoUrl();
    }

}
