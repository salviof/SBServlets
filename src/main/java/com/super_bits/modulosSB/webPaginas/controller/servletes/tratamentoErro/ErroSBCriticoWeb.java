package com.super_bits.modulosSB.webPaginas.controller.servletes.tratamentoErro;

import com.super_bits.modulosSB.SBCore.ConfigGeral.SBCore;

public class ErroSBCriticoWeb extends ErroSBGenericoWeb {

    public ErroSBCriticoWeb(String pMsg) {

        super(pMsg);

        if (!SBCore.isEmModoDesenvolvimento()) {
            //       FacesContext.getCurrentInstance().getExternalContext().redirect(SBWebPaginas.getSiteURL() + "/resources/SBComp/SBSystemPages/erroCriticoDeSistema.xhtml");
        } else {
            throw new UnsupportedOperationException("Ouve Lançamento de erro crítico para usuário:" + pMsg);
        }
    }

}
