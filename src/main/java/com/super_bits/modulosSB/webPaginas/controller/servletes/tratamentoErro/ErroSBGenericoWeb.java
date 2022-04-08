package com.super_bits.modulosSB.webPaginas.controller.servletes.tratamentoErro;

public class ErroSBGenericoWeb extends Throwable {

    public ErroSBGenericoWeb(Throwable throwable) {
        super(throwable);

    }

    public ErroSBGenericoWeb(String s, Throwable throwable) {
        super(s, throwable);

    }

    public ErroSBGenericoWeb(String pMsg) {
        super(pMsg);

    }
}
