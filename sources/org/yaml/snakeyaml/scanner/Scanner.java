package org.yaml.snakeyaml.scanner;

import org.yaml.snakeyaml.tokens.Token;

/* loaded from: snakeyaml-1.17.jar:org/yaml/snakeyaml/scanner/Scanner.class */
public interface Scanner {
    boolean checkToken(Token.ID... idArr);

    Token peekToken();

    Token getToken();
}
