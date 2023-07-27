/*
 *  Desenvolvido pela equipe Super-Bits.com CNPJ 20.019.971/0001-90

 */
package com.super_bits.modulosSB.webPaginas.controller.servletes.urls;

import com.super_bits.modulosSB.webPaginas.controller.servletes.util.UtilFabUrlServlet;
import com.super_bits.modulosSB.SBCore.ConfigGeral.SBCore;
import com.super_bits.modulosSB.SBCore.modulos.objetos.registro.Interfaces.basico.ItfBeanSimples;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.persistence.EntityManager;
import org.coletivojava.fw.api.tratamentoErros.FabErro;

/**
 *
 * @author desenvolvedor
 */
public class UrlInterpretada {

    private final Map<ItfFabUrlServletSBFW, ParteURLServlet> partes;

    private boolean partesObrigatoriasPreenchidas;

    public UrlInterpretada(Class<? extends ItfFabUrlServletSBFW> pFabrica, List<String> parametros, EntityManager pEm) throws UnsupportedOperationException {
        partes = new HashMap<>();

        if (UtilFabUrlServlet.possuiEntidade(pFabrica)) {
            if (pEm == null) {
                throw new UnsupportedOperationException("A url da fabrica " + pFabrica.getSimpleName() + "possui registro de entidade, é nescessário enviar um entity manager");
            }
        }
        try {

            for (ItfFabUrlServletSBFW slugDaParte : pFabrica.getEnumConstants()) {
                ParteURLServlet parte = slugDaParte.getParteURLAplicandoValorEnviado(parametros, pEm);
                partes.put(slugDaParte, parte);

            }
        } catch (Throwable t) {
            SBCore.RelatarErro(FabErro.SOLICITAR_REPARO, "Erro Criando Url Interpretada com parametros" + parametros, t);
            throw new UnsupportedOperationException("Erro criando Url interpretada");
        }
        defPartesObrigatoriasPreenchidas();

    }

    public void printParametrosComValoresInterpretados() {
        System.out.println("Parametros de url");

        for (Iterator<Map.Entry<ItfFabUrlServletSBFW, ParteURLServlet>> it = partes.entrySet().iterator(); it.hasNext();) {
            Entry<ItfFabUrlServletSBFW, ParteURLServlet> parteURL = it.next();
            if (parteURL.getValue() != null) {
                System.out.println(parteURL.getKey() + "--->" + parteURL.getValue().getValor().toString());
            } else {
                if (parteURL.getValue() != null) {
                    System.out.println(parteURL.getKey() + "--->" + parteURL.getValue().toString());
                } else {
                    System.out.println(parteURL.getKey() + "---> NULO");
                }
            }
        }
    }

    public UrlInterpretada(Class<? extends ItfFabUrlServletSBFW> pFabrica, List<String> parametros) throws UnsupportedOperationException {

        this(pFabrica, parametros, null);
    }

    private void defPartesObrigatoriasPreenchidas() {
        partesObrigatoriasPreenchidas = true;
        for (ParteURLServlet parte : partes.values()) {
            if (parte.isParametroObrigatorio()) {
                if (!parte.isValorDoParametroFoiConfigurado()) {
                    partesObrigatoriasPreenchidas = false;
                }
            }
        }

    }

    private boolean isPartesObrigatoriasPreenchidas() {
        return partesObrigatoriasPreenchidas;
    }

    public Object getValor(ItfFabUrlServletSBFW parametro) {
        try {
            return partes.get(parametro).getValor();
        } catch (Throwable t) {
            SBCore.RelatarErro(FabErro.SOLICITAR_REPARO, "Impossível obter valor do parameto" + parametro, t);
            return null;
        }
    }

    public String getValorComoString(ItfFabUrlServletSBFW parametro) {
        try {
            return (String) partes.get(parametro).getValor();
        } catch (Throwable t) {
            SBCore.RelatarErro(FabErro.SOLICITAR_REPARO, "Impossível obter valor do parameto como String" + parametro, t);
            return null;
        }
    }

    public ItfBeanSimples getValorComoBeanSimples(ItfFabUrlServletSBFW parametro) {
        try {
            return (ItfBeanSimples) partes.get(parametro).getValor();
        } catch (Throwable t) {
            SBCore.RelatarErro(FabErro.SOLICITAR_REPARO, "Impossível obter valor do parameto como Bean Simples" + parametro, t);
            return null;
        }
    }

}
