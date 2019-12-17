/*
 *  Desenvolvido pela equipe Super-Bits.com CNPJ 20.019.971/0001-90

 */
package com.super_bits.modulosSB.webPaginas.controller.servletes.urls.parametrosURL;

import com.super_bits.modulosSB.SBCore.ConfigGeral.SBCore;
import com.super_bits.modulosSB.SBCore.UtilGeral.UtilSBCoreStringFiltros;
import com.super_bits.modulosSB.SBCore.modulos.Controller.Interfaces.ItfEstruturaParametroRequisicao;
import com.super_bits.modulosSB.SBCore.modulos.Controller.Interfaces.ItfParametroRequisicao;
import com.super_bits.modulosSB.SBCore.modulos.Controller.Interfaces.TIPO_PARTE_URL;
import com.super_bits.modulosSB.SBCore.modulos.fabrica.FabObjetosAbstratos;
import com.super_bits.modulosSB.SBCore.modulos.fabrica.ItfFabrica;
import com.super_bits.modulosSB.SBCore.modulos.objetos.registro.Interfaces.basico.ItfBeanSimples;
import com.super_bits.modulosSB.webPaginas.controller.servletes.tratamentoErro.ErroSBCriticoWeb;

import java.util.HashMap;
import java.util.Map;
import org.coletivojava.fw.api.tratamentoErros.FabErro;

/**
 *
 * @author desenvolvedor
 */
public class ParametroUrlEstrutura implements ItfParametroRequisicao, ItfEstruturaParametroRequisicao {

    private final Object valorPadrao;
    private String slugValorPadrao;

    private final String nome;
    private final TIPO_PARTE_URL tipoParametro;
    private final Class tipoEntidade;

    private final boolean parametroObrigatorio;

    private boolean possuiFabricaDeObjetos = false;
    private final boolean umParametroDeEntidade;
    private Map<String, ItfBeanSimples> mapaObjetoPorString;

    private boolean umParametoEntidadeMBPrincipal;

    public Class<? extends ItfFabrica> fabricaObjetosRelacionada;

    public ParametroUrlEstrutura(InfoParametroURL pInfo) {
        nome = pInfo.nome();
        slugValorPadrao = pInfo.valorPadrao();
        tipoParametro = pInfo.tipoParametro();
        umParametoEntidadeMBPrincipal = pInfo.representaEntidadePrincipalMB();
        if (!pInfo.tipoEntidade().equals(void.class)) {
            tipoEntidade = pInfo.tipoEntidade();
            umParametroDeEntidade = true;
        } else {
            tipoEntidade = null;
            umParametroDeEntidade = false;
        }

        parametroObrigatorio = pInfo.obrigatorio();
        if (tipoParametro == TIPO_PARTE_URL.ENTIDADE && tipoEntidade == null) {

            try {
                throw new ErroSBCriticoWeb("Você precisa especificar o tipo de entidade para este tipo de parametro");
            } catch (ErroSBCriticoWeb e) {
                SBCore.RelatarErro(FabErro.SOLICITAR_REPARO, "Constructor Parametro URL" + pInfo.nome(), e);
            }

        }

        valorPadrao = null;
        if (tipoParametro == TIPO_PARTE_URL.OBJETO_COM_CONSTRUCTOR) {

            if (tipoEntidade == null) {

                try {
                    throw new ErroSBCriticoWeb("Erro configurando parametro " + pInfo.nome() + "O tipo de objeto  é obrigatório para parametros do tipo " + TIPO_PARTE_URL.OBJETO_COM_CONSTRUCTOR.toString() + "");
                } catch (ErroSBCriticoWeb e) {
                    SBCore.RelatarErro(FabErro.SOLICITAR_REPARO, "Constructor Parametro URL" + pInfo.nome(), e);
                }

            }

            if (!pInfo.fabricaObjetosRelacionada().equals(FabObjetosAbstratos.class)) {
                possuiFabricaDeObjetos = true;
                fabricaObjetosRelacionada = pInfo.fabricaObjetosRelacionada();
                mapaObjetoPorString = new HashMap<>();
                for (ItfFabrica fab : pInfo.fabricaObjetosRelacionada().getEnumConstants()) {
                    ItfBeanSimples objeto = (ItfBeanSimples) fab.getRegistro();
                    mapaObjetoPorString.put(UtilSBCoreStringFiltros.gerarUrlAmigavel(objeto.getNome()), objeto);
                }
            } else {

            }
        }

    }

    @Override
    public Class getClasseObjetoValor() {
        try {
            switch (tipoParametro) {
                case TEXTO:
                    return String.class;

                case ENTIDADE:
                    return getTipoEntidade();

                case OBJETO_COM_CONSTRUCTOR:

                    return getTipoEntidade();

                default:
                    throw new AssertionError(tipoParametro.name());

            }
        } catch (Throwable t) {
            SBCore.RelatarErro(FabErro.SOLICITAR_REPARO, "Erro determinando tipo de classe do parametro" + nome, t);
        }
        return null;
    }

    @Override
    public boolean isUmParametoEntidadeMBPrincipal() {
        return umParametoEntidadeMBPrincipal;
    }

    @Override
    public Object getValorPadrao() {
        return valorPadrao;
    }

    @Override
    public String getNome() {
        return nome;
    }

    @Override
    public TIPO_PARTE_URL getTipoParametro() {
        return tipoParametro;
    }

    @Override
    public Class getTipoEntidade() {
        return tipoEntidade;
    }

    @Override
    public boolean isParametroObrigatorio() {
        return parametroObrigatorio;
    }

    @Override
    public String getSlugValorPadrao() {

        return slugValorPadrao;
    }

    @Override
    public boolean isUmParametroDeEntidade() {
        return tipoParametro == TIPO_PARTE_URL.ENTIDADE;
    }

    @Override
    public boolean isPossuiFabricaDeObjetos() {
        if (fabricaObjetosRelacionada == null) {
            return false;
        }
        return !fabricaObjetosRelacionada.getSimpleName().equals(FabObjetosAbstratos.class.getSimpleName());

    }

    @Override
    public Object getObjetoPorNomeFabrica(String pValor) {
        if (pValor == null) {
            return null;
        }
        return mapaObjetoPorString.get(UtilSBCoreStringFiltros.gerarUrlAmigavel(pValor));
    }
}
