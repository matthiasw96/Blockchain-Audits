package org.hrw;

import org.hrw.backend.api.AuditAPI;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        AuditAPI api = new AuditAPI();

        api.start();
    }
}