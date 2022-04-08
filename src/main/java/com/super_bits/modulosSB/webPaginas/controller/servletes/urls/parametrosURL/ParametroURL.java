package com.super_bits.modulosSB.webPaginas.controller.servletes.urls.parametrosURL;

import com.super_bits.modulosSB.SBCore.modulos.Controller.Interfaces.ItfEstruturaParametroRequisicao;
import com.super_bits.modulosSB.SBCore.modulos.Controller.Interfaces.ItfParametroRequisicao;

/**
 *
 * @author Salvio
 */
public interface ParametroURL extends ItfParametroRequisicao {

    public default ItfEstruturaParametroRequisicao getEstrutura() {
        return (ItfEstruturaParametroRequisicao) this;
    }

}
