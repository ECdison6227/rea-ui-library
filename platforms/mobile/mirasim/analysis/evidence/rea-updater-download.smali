// [jadx java-decompile failed → smali]
// target: ai.mirofish.mirasim.ApkUpdaterPlugin.lambda$download$0
// markers: Method not decompiled; Method dump skipped, instruction units count
// (Java body was an anti-decompile stub; showing smali. Pass smali_fallback=false for the partial Java.)

.method private synthetic lambda$download$0(Ljava/lang/String;Lcom/getcapacitor/PluginCall;Ljava/lang/String;Ljava/lang/String;)V
    .registers 26

    move-object/from16 v1, p2

    .line 63
    const-string v2, "network"

    const-string/jumbo v3, "storage"

    .line 0
    const-string/jumbo v0, "update download failed: HTTP "

    .line 63
    new-instance v4, Ljava/io/File;

    invoke-virtual/range {p0 .. p0}, Lai/mirofish/mirasim/ApkUpdaterPlugin;->getContext()Landroid/content/Context;

    move-result-object v5

    invoke-virtual {v5}, Landroid/content/Context;->getCacheDir()Ljava/io/File;

    move-result-object v5

    const-string/jumbo v6, "updates"

    invoke-direct {v4, v5, v6}, Ljava/io/File;-><init>(Ljava/io/File;Ljava/lang/String;)V

    .line 64
    new-instance v5, Ljava/io/File;

    new-instance v6, Ljava/lang/StringBuilder;

    const-string v7, "mirasim-"

    invoke-direct {v6, v7}, Ljava/lang/StringBuilder;-><init>(Ljava/lang/String;)V

    move-object/from16 v7, p1

    invoke-virtual {v6, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v6

    const-string v7, ".apk"

    invoke-virtual {v6, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v6

    invoke-virtual {v6}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v6

    invoke-direct {v5, v4, v6}, Ljava/io/File;-><init>(Ljava/io/File;Ljava/lang/String;)V

    const/4 v6, 0x0

    .line 67
    :try_start_37
    invoke-virtual {v4}, Ljava/io/File;->exists()Z

    move-result v7

    if-nez v7, :cond_49

    invoke-virtual {v4}, Ljava/io/File;->mkdirs()Z

    move-result v7

    if-nez v7, :cond_49

    .line 68
    const-string v0, "cannot create update cache dir"

    invoke-virtual {v1, v0, v3}, Lcom/getcapacitor/PluginCall;->reject(Ljava/lang/String;Ljava/lang/String;)V

    return-void

    .line 73
    :cond_49
    invoke-static {v4}, Lai/mirofish/mirasim/ApkUpdaterPlugin;->purge(Ljava/io/File;)V

    .line 75
    new-instance v4, Ljava/net/URL;

    move-object/from16 v7, p3

    invoke-direct {v4, v7}, Ljava/net/URL;-><init>(Ljava/lang/String;)V

    invoke-virtual {v4}, Ljava/net/URL;->openConnection()Ljava/net/URLConnection;

    move-result-object v4

    check-cast v4, Ljava/net/HttpURLConnection;
    :try_end_59
    .catch Ljava/io/IOException; {:try_start_37 .. :try_end_59} :catch_1c6
    .catch Ljava/lang/Exception; {:try_start_37 .. :try_end_59} :catch_1b1
    .catchall {:try_start_37 .. :try_end_59} :catchall_1ad

    const/16 v6, 0x7530

    .line 76
    :try_start_5b
    invoke-virtual {v4, v6}, Ljava/net/HttpURLConnection;->setConnectTimeout(I)V

    const v6, 0xea60

    .line 77
    invoke-virtual {v4, v6}, Ljava/net/HttpURLConnection;->setReadTimeout(I)V

    const/4 v6, 0x1

    .line 78
    invoke-virtual {v4, v6}, Ljava/net/HttpURLConnection;->setInstanceFollowRedirects(Z)V

    .line 79
    invoke-virtual {v4}, Ljava/net/HttpURLConnection;->getResponseCode()I

    move-result v6

    const/16 v7, 0xc8

    if-lt v6, v7, :cond_172

    const/16 v7, 0x12c

    if-lt v6, v7, :cond_76

    goto/16 :goto_172

    .line 84
    :cond_76
    invoke-virtual {v4}, Ljava/net/HttpURLConnection;->getContentLengthLong()J

    move-result-wide v6
    :try_end_7a
    .catch Ljava/io/IOException; {:try_start_5b .. :try_end_7a} :catch_1a4
    .catch Ljava/lang/Exception; {:try_start_5b .. :try_end_7a} :catch_19b
    .catchall {:try_start_5b .. :try_end_7a} :catchall_193

    const-wide/32 v8, 0x20000000

    cmp-long v0, v6, v8

    if-lez v0, :cond_9f

    .line 86
    :try_start_81
    const-string/jumbo v0, "refusing an implausibly large APK"

    invoke-virtual {v1, v0, v3}, Lcom/getcapacitor/PluginCall;->reject(Ljava/lang/String;Ljava/lang/String;)V
    :try_end_87
    .catch Ljava/io/IOException; {:try_start_81 .. :try_end_87} :catch_9b
    .catch Ljava/lang/Exception; {:try_start_81 .. :try_end_87} :catch_97
    .catchall {:try_start_81 .. :try_end_87} :catchall_91

    if-eqz v4, :cond_8d

    .line 128
    invoke-virtual {v4}, Ljava/net/HttpURLConnection;->disconnect()V

    return-void

    :cond_8d
    move-object/from16 v5, p0

    goto/16 :goto_1dd

    :catchall_91
    move-exception v0

    move-object/from16 v5, p0

    move-object v6, v4

    goto/16 :goto_1df

    :catch_97
    move-exception v0

    move-object v6, v4

    goto/16 :goto_1b2

    :catch_9b
    move-exception v0

    move-object v6, v4

    goto/16 :goto_1c7

    .line 90
    :cond_9f
    :try_start_9f
    const-string v0, "SHA-256"

    invoke-static {v0}, Ljava/security/MessageDigest;->getInstance(Ljava/lang/String;)Ljava/security/MessageDigest;

    move-result-object v0

    const/high16 v10, 0x10000

    .line 92
    new-array v10, v10, [B

    .line 93
    invoke-virtual {v4}, Ljava/net/HttpURLConnection;->getInputStream()Ljava/io/InputStream;

    move-result-object v11
    :try_end_ad
    .catch Ljava/io/IOException; {:try_start_9f .. :try_end_ad} :catch_1a4
    .catch Ljava/lang/Exception; {:try_start_9f .. :try_end_ad} :catch_19b
    .catchall {:try_start_9f .. :try_end_ad} :catchall_193

    :try_start_ad
    new-instance v12, Ljava/io/FileOutputStream;

    invoke-direct {v12, v5}, Ljava/io/FileOutputStream;-><init>(Ljava/io/File;)V
    :try_end_b2
    .catchall {:try_start_ad .. :try_end_b2} :catchall_160

    const-wide/16 v13, 0x0

    move-wide/from16 v17, v8

    move-wide v15, v13

    .line 95
    :goto_b7
    :try_start_b7
    invoke-virtual {v11, v10}, Ljava/io/InputStream;->read([B)I

    move-result v8

    const/4 v9, -0x1

    if-eq v8, v9, :cond_109

    const/4 v9, 0x0

    .line 96
    invoke-virtual {v12, v10, v9, v8}, Ljava/io/FileOutputStream;->write([BII)V

    .line 97
    invoke-virtual {v0, v10, v9, v8}, Ljava/security/MessageDigest;->update([BII)V

    int-to-long v8, v8

    add-long/2addr v8, v15

    cmp-long v15, v8, v17

    if-gtz v15, :cond_fa

    cmp-long v15, v6, v13

    if-lez v15, :cond_ee

    .line 103
    new-instance v15, Lcom/getcapacitor/JSObject;

    invoke-direct {v15}, Lcom/getcapacitor/JSObject;-><init>()V

    .line 104
    const-string/jumbo v13, "progress"
    :try_end_d7
    .catchall {:try_start_b7 .. :try_end_d7} :catchall_150

    move-object/from16 p1, v4

    move-object v14, v5

    long-to-double v4, v8

    move-wide/from16 v19, v4

    long-to-double v4, v6

    div-double v4, v19, v4

    :try_start_e0
    invoke-virtual {v15, v13, v4, v5}, Lcom/getcapacitor/JSObject;->put(Ljava/lang/String;D)Lcom/getcapacitor/JSObject;

    .line 105
    const-string v4, "downloadProgress"
    :try_end_e5
    .catchall {:try_start_e0 .. :try_end_e5} :catchall_eb

    move-object/from16 v5, p0

    :try_start_e7
    invoke-virtual {v5, v4, v15}, Lai/mirofish/mirasim/ApkUpdaterPlugin;->notifyListeners(Ljava/lang/String;Lcom/getcapacitor/JSObject;)V

    goto :goto_f3

    :catchall_eb
    move-exception v0

    goto/16 :goto_154

    :cond_ee
    move-object/from16 p1, v4

    move-object v14, v5

    move-object/from16 v5, p0

    :goto_f3
    move-object/from16 v4, p1

    move-wide v15, v8

    move-object v5, v14

    const-wide/16 v13, 0x0

    goto :goto_b7

    :cond_fa
    move-object/from16 p1, v4

    move-object v14, v5

    move-object/from16 v5, p0

    .line 100
    new-instance v0, Ljava/lang/IllegalStateException;

    const-string v4, "APK exceeded the size ceiling mid-stream"

    invoke-direct {v0, v4}, Ljava/lang/IllegalStateException;-><init>(Ljava/lang/String;)V

    throw v0
    :try_end_107
    .catchall {:try_start_e7 .. :try_end_107} :catchall_107

    :catchall_107
    move-exception v0

    goto :goto_156

    :cond_109
    move-object/from16 p1, v4

    move-object v14, v5

    move-object/from16 v5, p0

    .line 108
    :try_start_10e
    invoke-virtual {v12}, Ljava/io/FileOutputStream;->close()V
    :try_end_111
    .catchall {:try_start_10e .. :try_end_111} :catchall_14e

    if-eqz v11, :cond_116

    :try_start_113
    invoke-virtual {v11}, Ljava/io/InputStream;->close()V

    .line 110
    :cond_116
    invoke-virtual {v0}, Ljava/security/MessageDigest;->digest()[B

    move-result-object v0

    invoke-static {v0}, Lai/mirofish/mirasim/ApkUpdaterPlugin;->hex([B)Ljava/lang/String;

    move-result-object v0

    move-object/from16 v4, p4

    .line 111
    invoke-virtual {v0, v4}, Ljava/lang/String;->equalsIgnoreCase(Ljava/lang/String;)Z

    move-result v0

    if-nez v0, :cond_136

    .line 113
    invoke-static {v14}, Lai/mirofish/mirasim/ApkUpdaterPlugin;->deleteQuietly(Ljava/io/File;)V

    .line 114
    const-string v0, "checksum mismatch"

    const-string v4, "checksum"

    invoke-virtual {v1, v0, v4}, Lcom/getcapacitor/PluginCall;->reject(Ljava/lang/String;Ljava/lang/String;)V
    :try_end_130
    .catch Ljava/io/IOException; {:try_start_113 .. :try_end_130} :catch_191
    .catch Ljava/lang/Exception; {:try_start_113 .. :try_end_130} :catch_18f
    .catchall {:try_start_113 .. :try_end_130} :catchall_18d

    if-eqz p1, :cond_1dd

    .line 128
    invoke-virtual/range {p1 .. p1}, Ljava/net/HttpURLConnection;->disconnect()V

    return-void

    .line 118
    :cond_136
    :try_start_136
    new-instance v0, Lcom/getcapacitor/JSObject;

    invoke-direct {v0}, Lcom/getcapacitor/JSObject;-><init>()V

    .line 119
    const-string/jumbo v4, "path"

    invoke-virtual {v14}, Ljava/io/File;->getAbsolutePath()Ljava/lang/String;

    move-result-object v6

    invoke-virtual {v0, v4, v6}, Lcom/getcapacitor/JSObject;->put(Ljava/lang/String;Ljava/lang/String;)Lcom/getcapacitor/JSObject;

    .line 120
    invoke-virtual {v1, v0}, Lcom/getcapacitor/PluginCall;->resolve(Lcom/getcapacitor/JSObject;)V
    :try_end_148
    .catch Ljava/io/IOException; {:try_start_136 .. :try_end_148} :catch_191
    .catch Ljava/lang/Exception; {:try_start_136 .. :try_end_148} :catch_18f
    .catchall {:try_start_136 .. :try_end_148} :catchall_18d

    if-eqz p1, :cond_1dd

    .line 128
    invoke-virtual/range {p1 .. p1}, Ljava/net/HttpURLConnection;->disconnect()V

    return-void

    :catchall_14e
    move-exception v0

    goto :goto_166

    :catchall_150
    move-exception v0

    move-object/from16 p1, v4

    move-object v14, v5

    :goto_154
    move-object/from16 v5, p0

    :goto_156
    move-object v4, v0

    .line 93
    :try_start_157
    invoke-virtual {v12}, Ljava/io/FileOutputStream;->close()V
    :try_end_15a
    .catchall {:try_start_157 .. :try_end_15a} :catchall_15b

    goto :goto_15f

    :catchall_15b
    move-exception v0

    :try_start_15c
    invoke-virtual {v4, v0}, Ljava/lang/Throwable;->addSuppressed(Ljava/lang/Throwable;)V

    :goto_15f
    throw v4
    :try_end_160
    .catchall {:try_start_15c .. :try_end_160} :catchall_14e

    :catchall_160
    move-exception v0

    move-object/from16 p1, v4

    move-object v14, v5

    move-object/from16 v5, p0

    :goto_166
    move-object v4, v0

    if-eqz v11, :cond_171

    :try_start_169
    invoke-virtual {v11}, Ljava/io/InputStream;->close()V
    :try_end_16c
    .catchall {:try_start_169 .. :try_end_16c} :catchall_16d

    goto :goto_171

    :catchall_16d
    move-exception v0

    :try_start_16e
    invoke-virtual {v4, v0}, Ljava/lang/Throwable;->addSuppressed(Ljava/lang/Throwable;)V

    :cond_171
    :goto_171
    throw v4

    :cond_172
    :goto_172
    move-object/from16 p1, v4

    move-object v14, v5

    move-object/from16 v5, p0

    .line 81
    new-instance v4, Ljava/lang/StringBuilder;

    invoke-direct {v4, v0}, Ljava/lang/StringBuilder;-><init>(Ljava/lang/String;)V

    invoke-virtual {v4, v6}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    move-result-object v0

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {v1, v0, v2}, Lcom/getcapacitor/PluginCall;->reject(Ljava/lang/String;Ljava/lang/String;)V
    :try_end_187
    .catch Ljava/io/IOException; {:try_start_16e .. :try_end_187} :catch_191
    .catch Ljava/lang/Exception; {:try_start_16e .. :try_end_187} :catch_18f
    .catchall {:try_start_16e .. :try_end_187} :catchall_18d

    if-eqz p1, :cond_1dd

    .line 128
    invoke-virtual/range {p1 .. p1}, Ljava/net/HttpURLConnection;->disconnect()V

    return-void

    :catchall_18d
    move-exception v0

    goto :goto_198

    :catch_18f
    move-exception v0

    goto :goto_1a1

    :catch_191
    move-exception v0

    goto :goto_1aa

    :catchall_193
    move-exception v0

    move-object/from16 v5, p0

    move-object/from16 p1, v4

    :goto_198
    move-object/from16 v6, p1

    goto :goto_1df

    :catch_19b
    move-exception v0

    move-object/from16 p1, v4

    move-object v14, v5

    move-object/from16 v5, p0

    :goto_1a1
    move-object/from16 v6, p1

    goto :goto_1b5

    :catch_1a4
    move-exception v0

    move-object/from16 p1, v4

    move-object v14, v5

    move-object/from16 v5, p0

    :goto_1aa
    move-object/from16 v6, p1

    goto :goto_1ca

    :catchall_1ad
    move-exception v0

    move-object/from16 v5, p0

    goto :goto_1df

    :catch_1b1
    move-exception v0

    :goto_1b2
    move-object v14, v5

    move-object/from16 v5, p0

    .line 125
    :goto_1b5
    :try_start_1b5
    invoke-static {v14}, Lai/mirofish/mirasim/ApkUpdaterPlugin;->deleteQuietly(Ljava/io/File;)V

    .line 126
    invoke-virtual {v0}, Ljava/lang/Exception;->getMessage()Ljava/lang/String;

    move-result-object v0

    invoke-static {v0}, Ljava/lang/String;->valueOf(Ljava/lang/Object;)Ljava/lang/String;

    move-result-object v0

    invoke-virtual {v1, v0, v3}, Lcom/getcapacitor/PluginCall;->reject(Ljava/lang/String;Ljava/lang/String;)V

    if-eqz v6, :cond_1dd

    goto :goto_1da

    :catch_1c6
    move-exception v0

    :goto_1c7
    move-object v14, v5

    move-object/from16 v5, p0

    .line 122
    :goto_1ca
    invoke-static {v14}, Lai/mirofish/mirasim/ApkUpdaterPlugin;->deleteQuietly(Ljava/io/File;)V

    .line 123
    invoke-virtual {v0}, Ljava/io/IOException;->getMessage()Ljava/lang/String;

    move-result-object v0

    invoke-static {v0}, Ljava/lang/String;->valueOf(Ljava/lang/Object;)Ljava/lang/String;

    move-result-object v0

    invoke-virtual {v1, v0, v2}, Lcom/getcapacitor/PluginCall;->reject(Ljava/lang/String;Ljava/lang/String;)V
    :try_end_1d8
    .catchall {:try_start_1b5 .. :try_end_1d8} :catchall_1de

    if-eqz v6, :cond_1dd

    .line 128
    :goto_1da
    invoke-virtual {v6}, Ljava/net/HttpURLConnection;->disconnect()V

    :cond_1dd
    :goto_1dd
    return-void

    :catchall_1de
    move-exception v0

    :goto_1df
    if-eqz v6, :cond_1e4

    invoke-virtual {v6}, Ljava/net/HttpURLConnection;->disconnect()V

    .line 129
    :cond_1e4
    throw v0
.end method