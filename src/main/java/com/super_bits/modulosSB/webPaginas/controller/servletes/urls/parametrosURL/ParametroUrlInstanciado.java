/*
 *  Desenvolvido pela equipe Super-Bits.com CNPJ 20.019.971/0001-90

 */
package com.super_bits.modulosSB.webPaginas.controller.servletes.urls.parametrosURL;

import com.super_bits.modulosSB.Persistencia.ConfigGeral.SBPersistencia;
import com.super_bits.modulosSB.Persistencia.dao.UtilSBPersistencia;
import com.super_bits.modulosSB.SBCore.ConfigGeral.SBCore;
import com.super_bits.modulosSB.SBCore.UtilGeral.UtilCRCStringValidador;
import com.super_bits.modulosSB.SBCore.modulos.Controller.Interfaces.ItfEstruturaParametroRequisicao;
import com.super_bits.modulosSB.SBCore.modulos.Controller.Interfaces.ItfParametroRequisicaoInstanciado;
import com.super_bits.modulosSB.SBCore.modulos.Controller.Interfaces.TIPO_PARTE_URL;
import static com.super_bits.modulosSB.SBCore.modulos.Controller.Interfaces.TIPO_PARTE_URL.ENTIDADE;
import static com.super_bits.modulosSB.SBCore.modulos.Controller.Interfaces.TIPO_PARTE_URL.OBJETO_COM_CONSTRUCTOR;
import static com.super_bits.modulosSB.SBCore.modulos.Controller.Interfaces.TIPO_PARTE_URL.TEXTO;
import com.super_bits.modulosSB.SBCore.modulos.objetos.registro.Interfaces.basico.ComoEntidadeSimples;
import com.super_bits.modulosSB.SBCore.modulos.objetos.registro.Interfaces.basico.ComoEntidadeSimplesSomenteLeitura;
import com.super_bits.modulosSB.SBCore.modulos.objetos.registro.Interfaces.basico.ComoEntidadeVinculadoAEnum;
import com.super_bits.modulosSB.webPaginas.controller.servletes.util.UtilFabUrlServlet;

import javax.persistence.EntityManager;
import org.coletivojava.fw.api.tratamentoErros.FabErro;

/**
 *
 * @author desenvolvedor
 */
public class ParametroUrlInstanciado implements ItfParametroRequisicaoInstanciado,
        ParametroURL {

    private final ItfEstruturaParametroRequisicao estruturaParametro;
    private Object valor;
    private String parteURLenviada;

    public ParametroUrlInstanciado(ItfEstruturaParametroRequisicao pParametro) {

        this.estruturaParametro = pParametro;
    }

    @Override
    public ItfEstruturaParametroRequisicao getEstrutura() {
        return estruturaParametro;
    }

    public ParametroUrlInstanciado(InfoParametroURL pInfoParametro) {
        try {
            this.estruturaParametro = new ParametroUrlEstrutura(pInfoParametro);
            //        System.out.println(estruturaParametro.isUmParametoEntidadeMBPrincipal());
        } catch (Throwable t) {
            SBCore.RelatarErro(FabErro.SOLICITAR_REPARO, "Erro instanciando Parametro com anotação" + pInfoParametro, t);
            if (pInfoParametro != null) {
                throw new UnsupportedOperationException("Errp instanciando parametro" + pInfoParametro.nome() + " " + pInfoParametro.fabricaObjetosRelacionada().getName());
            } else {
                throw new UnsupportedOperationException("Erro tentando instanciar parametro com informações nulas do parametro");
            }
        }
    }

    /**
     *
     * @return True se o parametro foi enviado na URL
     */
    @Override
    public boolean isValorDoParametroFoiConfigurado() {
        return UtilCRCStringValidador.isNAO_NuloNemBranco(parteURLenviada);
    }

    @Override
    public Object getValor() {
        return valor;
    }

    @Override
    public void setValor(Object pValor) {
        valor = pValor;
    }

    @Override
    public String getNome() {
        return estruturaParametro.getNome();
    }

    @Override
    public Class getTipoEntidade() {
        return estruturaParametro.getTipoEntidade();
    }

    @Override
    public TIPO_PARTE_URL getTipoParametro() {
        return estruturaParametro.getTipoParametro();
    }

    @Override
    public Object getValorPadrao() {
        return estruturaParametro.getValorPadrao();
    }

    @Override
    public boolean isParametroObrigatorio() {
        return estruturaParametro.isParametroObrigatorio();
    }

    @Override
    public boolean isUmParametroDeEntidade() {
        return estruturaParametro.isUmParametroDeEntidade();
    }

    @Override
    public boolean isUmParametoEntidadeMBPrincipal() {
        return estruturaParametro.isUmParametoEntidadeMBPrincipal();
    }

    @Override
    public String getSlugValorParametro() {

        if (valor == null) {
            return estruturaParametro.getSlugValorPadrao();
        } else {
            switch (estruturaParametro.getTipoParametro()) {
                case TEXTO:
                    return (String) valor;

                case ENTIDADE:
                    return UtilFabUrlServlet.getSlugDoObjeto((ComoEntidadeSimples) valor);

                case OBJETO_COM_CONSTRUCTOR:
                    if (valor instanceof ComoEntidadeVinculadoAEnum) {
                        ComoEntidadeVinculadoAEnum obj = (ComoEntidadeVinculadoAEnum) valor;
                        if (obj.getEnumVinculado() != null) {
                            return obj.getEnumVinculado().getClass().getSimpleName() + "." + obj.getEnumVinculado().toString();
                        }
                    }
                    if (valor instanceof ComoEntidadeSimplesSomenteLeitura) {
                        ComoEntidadeSimplesSomenteLeitura obj = (ComoEntidadeSimplesSomenteLeitura) valor;
                        return obj.getNome();
                    }
                    break;
                default:
                    return estruturaParametro.getSlugValorPadrao();

            }
        }
        return estruturaParametro.getSlugValorPadrao();

    }

    @Override
    public void aplicarParteURLenviada(String pParteEnviada, EntityManager pEm) throws Throwable {
        try {
            parteURLenviada = pParteEnviada;
            if (pParteEnviada == null) {
                throw new UnsupportedOperationException("Tentativa de aplicar parte de URL igual a nulo, você precisa tratar isso.");
            }

            switch (getTipoParametro()) {
                case ENTIDADE:

                    ComoEntidadeSimples registroByURL = null;
                    try {
                        if (SBPersistencia.isConfigurado()) {
                            registroByURL = (ComoEntidadeSimples) UtilSBPersistencia.getRegistroByNomeSlug(getTipoEntidade(), (String) pParteEnviada, pEm);
                        } else {
                            System.out.println("O parametro de URL " + getNome() + " é do tipo Entidade, porém este projeto não tem Persistencia configurada");
                        }
                    } catch (Exception e) {
                        SBCore.RelatarErro(FabErro.SOLICITAR_REPARO, "Erro obtendo registro de parametroURL de entidade pela URL", e);
                    }

                    if (registroByURL == null) {
                        // Se o Parametro não foi setado ainda Utiliza o valor Padrão

                        if (getValor() == null) {
                            if (estruturaParametro.isParametroObrigatorio()) {
                                SBCore.RelatarErro(FabErro.SOLICITAR_REPARO, "Não encontrado Registro" + getValor() + " do tipo" + getTipoEntidade().getSimpleName(), null);
                                setValor(getValorPadrao());
                            }
                            // caso contrario renova o Objeto
                        } else {
                            ComoEntidadeSimples registroRenovado = (ComoEntidadeSimples) UtilSBPersistencia.getRegistroByID((Class<? extends ComoEntidadeSimples>) getValor().getClass(), ((ComoEntidadeSimples) getValor()).getId(), pEm);
                            setValor(registroRenovado);
                        }

                    } else {
                        setValor(registroByURL);
                    }
                    break;
                case TEXTO:
                    setValor(pParteEnviada);

                    break;
                case OBJETO_COM_CONSTRUCTOR:
                    System.out.println("obtendo objeto estatico por string:" + pParteEnviada);
                    Object obj = null;
                    if (estruturaParametro.isPossuiFabricaDeObjetos()) {

                        {
                            obj = estruturaParametro.getObjetoPorNomeFabrica(pParteEnviada);
                        }
                        if (obj == null) {

                            obj = SBCore.getObjetoEstatico(pParteEnviada);
                        }
                    }

                    if (obj != null) {
                        setValor(obj);
                    } else {
                        throw new UnsupportedOperationException("Objeto com constructor não pode ser Obtido pela String" + pParteEnviada);
                    }

            }
        } catch (Throwable t) {
            String conteudoParteURLErro = parteURLenviada;
            parteURLenviada = null;
            SBCore.RelatarErro(FabErro.SOLICITAR_REPARO, "Erro Aplicando parte de url" + getNome() + " o conteúdo enviado para setar o valor foi:" + conteudoParteURLErro, t);
            throw new UnsupportedOperationException("Erro aplicando parte de url Enviada em parametro" + getNome() + " o conteúdo enviado para setar o valor foi:" + conteudoParteURLErro, t);
        }

    }

    @Override
    public boolean isPossuiFabricaDeObjetos() {
        return estruturaParametro.isPossuiFabricaDeObjetos();
    }

    public void aplicarParteURLenviada(String pParteEnviada) throws Throwable {
        aplicarParteURLenviada(pParteEnviada, null);
    }

    @Override
    public Class getClasseObjetoValor() {
        return estruturaParametro.getClasseObjetoValor();
    }

    @Override
    public String getSlugValorPadrao() {
        return estruturaParametro.getSlugValorPadrao();
    }

    @Override
    public Object getObjetoPorNomeFabrica(String pValor) {
        return estruturaParametro.getObjetoPorNomeFabrica(pValor);
    }

    @Override
    public String getTextoEnviadoUrl() {
        return parteURLenviada;
    }

}
