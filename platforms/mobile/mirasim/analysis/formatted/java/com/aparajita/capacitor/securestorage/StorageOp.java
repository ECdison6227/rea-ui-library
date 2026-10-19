package com.aparajita.capacitor.securestorage;

import java.io.IOException;
import java.security.GeneralSecurityException;

/* JADX INFO: compiled from: SecureStorage.java */
/* JADX INFO: loaded from: classes2.dex */
interface StorageOp {
    void run() throws KeyStoreException, GeneralSecurityException, IOException;
}