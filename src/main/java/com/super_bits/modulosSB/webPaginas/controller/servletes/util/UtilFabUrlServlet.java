/*
 *  Desenvolvido pela equipe Super-Bits.com CNPJ 20.019.971/0001-90

 */
package com.super_bits.modulosSB.webPaginas.controller.servletes.util;

import com.google.common.collect.Lists;
import com.super_bits.modulosSB.Persistencia.dao.UtilSBPersistencia;
import com.super_bits.modulosSB.SBCore.ConfigGeral.SBCore;
import com.super_bits.modulosSB.SBCore.UtilGeral.UtilSBCoreStringFiltros;
import com.super_bits.modulosSB.SBCore.modulos.Controller.Interfaces.TIPO_PARTE_URL;
import com.super_bits.modulosSB.SBCore.modulos.objetos.registro.Interfaces.basico.ComoEntidadeSimples;
import com.super_bits.modulosSB.webPaginas.controller.servletes.urls.ItfFabUrlServletSBFW;
import com.super_bits.modulosSB.webPaginas.controller.servletes.urls.ParteURLServlet;
import com.super_bits.modulosSB.webPaginas.controller.servletes.urls.UrlInterpretada;
import com.super_bits.modulosSB.webPaginas.controller.servletes.urls.parametrosURL.InfoParametroURL;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import org.coletivojava.fw.api.tratamentoErros.FabErro;

/**
 *
 * @author desenvolvedor
 */
public class UtilFabUrlServlet {

    private static final Map<ItfFabUrlServletSBFW, ParteURLServlet> partesCarregadas = new HashMap<>();
    private static final Map< Class< ? extends ItfFabUrlServletSBFW>, Boolean> mapaPossuiEntidade = new HashMap<>();

    public static ParteURLServlet getParteURL(final ItfFabUrlServletSBFW pParteURL) {
        try {
            Field cp = pParteURL.getClass().getField(pParteURL.toString());
            ParteURLServlet parte = new ParteURLServlet(getInfoParametroDeUrl(cp));
            return parte;
        } catch (Throwable t) {
            if (pParteURL != null) {
                SBCore.RelatarErro(FabErro.SOLICITAR_REPARO, "Erro criando parte de url da fabrica" + pParteURL.getClass().getSimpleName() + "-" + pParteURL, t);
            } else {
                SBCore.RelatarErro(FabErro.SOLICITAR_REPARO, "Erro criando parte de url, a Fabrica não foi enviada!! impossível determinar a parte", t);
            }
            return null;
        }

    }

    public static InfoParametroURL getInfoParametroDeUrl(Field cp) {
        InfoParametroURL infoPr = cp.getDeclaredAnnotation(InfoParametroURL.class);
        if (infoPr == null) {
            throw new UnsupportedOperationException("Erro o parametro " + cp.getName() + " não foi anotado com @InfoParametro em" + cp.getDeclaringClass().getSimpleName());
        }
        return infoPr;
    }

    public static boolean possuiEntidade(Class< ? extends ItfFabUrlServletSBFW> pfabricaURL) {
        try {
            Boolean possuiEntidade = mapaPossuiEntidade.get(pfabricaURL);
            if (possuiEntidade != null) {
                return possuiEntidade;
            }
            for (ItfFabUrlServletSBFW pFabrica : pfabricaURL.getEnumConstants()) {

                Field campo = pFabrica.getClass().getField(pFabrica.toString());
                InfoParametroURL infoPr = getInfoParametroDeUrl(campo);
                if (infoPr.tipoParametro().equals(TIPO_PARTE_URL.ENTIDADE)) {
                    mapaPossuiEntidade.put(pfabricaURL, true);
                    return true;
                }

            }
            mapaPossuiEntidade.put(pfabricaURL, false);
            return false;
        } catch (Throwable t) {
            SBCore.RelatarErro(FabErro.SOLICITAR_REPARO, "Erro verificando se a Fabrica de URL possui registro de entidade", t);
            return false;
        }
    }

    public static UrlInterpretada getUrlInterpretada(Class<? extends ItfFabUrlServletSBFW> fabrica,
            HttpServletRequest requisicao
    ) throws Throwable {

        try {
            List<String> slugsURL = getListaStringsParametroURL(requisicao);
            if (possuiEntidade(fabrica)) {
                EntityManager em = UtilSBPersistencia.getNovoEM();
                return new UrlInterpretada(fabrica, slugsURL, em);
            } else {
                return new UrlInterpretada(fabrica, slugsURL);
            }
        } catch (Throwable t) {
            String texto = "Erro interpretando urls a partir da classe" + fabrica.getSimpleName() + " com o caminho de url=" + getSlugsDeUrl(requisicao);
            SBCore.RelatarErro(FabErro.SOLICITAR_REPARO, "Erro Interpretando Urls", t);
            throw new UnsupportedOperationException("Erro interpretando url");
        }

    }

    public static List<String> getListaStringsParametroURL(HttpServletRequest requisicao) {
        List<String> slugsURL = getSlugsDeUrl(requisicao);

        return slugsURL;
    }

    /**
     *
     * Retorna as slugs da url utilizada na requisição em um array
     *
     * Ex:
     * www.portalLegalPraBctaQueDesenvolvi.com.br/arquivos/imagens/imagem.jpg
     *
     * Retronaria: um array: [arquivos,imagens,imagem.jpg]
     *
     * @param requisicao
     * @return
     */
    public static List<String> getSlugsDeUrl(HttpServletRequest requisicao) {
        String caminhoCOmpleto = "";
        try {

            String caminhoSemURL = requisicao.getRequestURI();
            caminhoSemURL = caminhoSemURL.replace(".html", "");
            if (caminhoSemURL.startsWith("/")) {
                caminhoSemURL = caminhoSemURL.substring(1);
            }
            if (caminhoSemURL == null) {
                return new ArrayList<>();
            }
            return Lists.newArrayList(Arrays.asList(caminhoSemURL.split("/")));
        } catch (Throwable t) {
            SBCore.RelatarErro(FabErro.SOLICITAR_REPARO, "Erro obtendo parametros da url, requisicao " + requisicao + " url=" + caminhoCOmpleto, t);
            return null;
        }
    }

    public static String getSlugDoObjeto(ComoEntidadeSimples objSimples) {

        return UtilSBCoreStringFiltros.gerarUrlAmigavel(objSimples.getNome())
                + "-" + String.valueOf(objSimples.getId());
    }

    public static void getEstruturaDePaginaPorRequisicao(HttpServletRequest requisicao) {

    }

}
