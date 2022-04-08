/*
 *  Desenvolvido pela equipe Super-Bits.com CNPJ 20.019.971/0001-90

 */
package com.super_bits.modulosSB.webPaginas.controller.servletes.urls;

import com.super_bits.modulosSB.webPaginas.controller.servletes.util.UtilFabUrlServlet;
import com.super_bits.modulosSB.SBCore.ConfigGeral.SBCore;
import java.util.List;
import javax.persistence.EntityManager;
import org.coletivojava.fw.api.tratamentoErros.FabErro;

/**
 *
 * @author desenvolvedor
 */
public interface ItfFabUrlServletSBFW {

    public default ParteURLServlet getParteURLAplicandoValorEnviado(List<String> parametrosURL, EntityManager pEm) {
        ParteURLServlet parte = UtilFabUrlServlet.getParteURL(this);
        try {
            int idx = ((Enum) this).ordinal();
            idx++;// primeiro parametro é o nome do servlet
            String parametro = parametrosURL.get(idx);
            parte.aplicarParteURLenviada(parametro, pEm);
        } catch (Throwable t) {
            if (parte.isParametroObrigatorio()) {
                System.out.println("Erro aplicando parametro obrigatório");
                SBCore.RelatarErro(FabErro.SOLICITAR_REPARO, "Erro:" + t.getMessage(), t);
                String parametrosDeURl = "parametros nulos";
                if (parametrosURL != null) {
                    for (String pr : parametrosURL) {
                        parametrosDeURl += "-" + pr;
                    }
                }

                throw new UnsupportedOperationException("O parametro obrigatório não foi enviado em " + parte.getNome() + " parametros=" + parametrosDeURl, t);
            }
        }
        return parte;
    }

}
