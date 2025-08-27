package org.springframework.expression.spel.standard;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import java.util.regex.Pattern;
import org.springframework.expression.ParseException;
import org.springframework.expression.ParserContext;
import org.springframework.expression.common.TemplateAwareExpressionParser;
import org.springframework.expression.spel.InternalParseException;
import org.springframework.expression.spel.SpelMessage;
import org.springframework.expression.spel.SpelParseException;
import org.springframework.expression.spel.SpelParserConfiguration;
import org.springframework.expression.spel.ast.Assign;
import org.springframework.expression.spel.ast.BeanReference;
import org.springframework.expression.spel.ast.BooleanLiteral;
import org.springframework.expression.spel.ast.CompoundExpression;
import org.springframework.expression.spel.ast.ConstructorReference;
import org.springframework.expression.spel.ast.Elvis;
import org.springframework.expression.spel.ast.FunctionReference;
import org.springframework.expression.spel.ast.Identifier;
import org.springframework.expression.spel.ast.Indexer;
import org.springframework.expression.spel.ast.InlineList;
import org.springframework.expression.spel.ast.InlineMap;
import org.springframework.expression.spel.ast.Literal;
import org.springframework.expression.spel.ast.MethodReference;
import org.springframework.expression.spel.ast.NullLiteral;
import org.springframework.expression.spel.ast.OpAnd;
import org.springframework.expression.spel.ast.OpDec;
import org.springframework.expression.spel.ast.OpDivide;
import org.springframework.expression.spel.ast.OpEQ;
import org.springframework.expression.spel.ast.OpGE;
import org.springframework.expression.spel.ast.OpGT;
import org.springframework.expression.spel.ast.OpInc;
import org.springframework.expression.spel.ast.OpLE;
import org.springframework.expression.spel.ast.OpLT;
import org.springframework.expression.spel.ast.OpMinus;
import org.springframework.expression.spel.ast.OpModulus;
import org.springframework.expression.spel.ast.OpMultiply;
import org.springframework.expression.spel.ast.OpNE;
import org.springframework.expression.spel.ast.OpOr;
import org.springframework.expression.spel.ast.OpPlus;
import org.springframework.expression.spel.ast.OperatorBetween;
import org.springframework.expression.spel.ast.OperatorInstanceof;
import org.springframework.expression.spel.ast.OperatorMatches;
import org.springframework.expression.spel.ast.OperatorNot;
import org.springframework.expression.spel.ast.OperatorPower;
import org.springframework.expression.spel.ast.Projection;
import org.springframework.expression.spel.ast.PropertyOrFieldReference;
import org.springframework.expression.spel.ast.QualifiedIdentifier;
import org.springframework.expression.spel.ast.Selection;
import org.springframework.expression.spel.ast.SpelNodeImpl;
import org.springframework.expression.spel.ast.StringLiteral;
import org.springframework.expression.spel.ast.Ternary;
import org.springframework.expression.spel.ast.TypeReference;
import org.springframework.expression.spel.ast.VariableReference;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/* loaded from: spring-expression-4.3.25.RELEASE.jar:org/springframework/expression/spel/standard/InternalSpelExpressionParser.class */
class InternalSpelExpressionParser extends TemplateAwareExpressionParser {
    private static final Pattern VALID_QUALIFIED_ID_PATTERN = Pattern.compile("[\\p{L}\\p{N}_$]+");
    private final SpelParserConfiguration configuration;
    private final Stack<SpelNodeImpl> constructedNodes = new Stack<>();
    private String expressionString;
    private List<Token> tokenStream;
    private int tokenStreamLength;
    private int tokenStreamPointer;

    public InternalSpelExpressionParser(SpelParserConfiguration configuration) {
        this.configuration = configuration;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.springframework.expression.common.TemplateAwareExpressionParser
    public SpelExpression doParseExpression(String expressionString, ParserContext context) throws ParseException {
        try {
            this.expressionString = expressionString;
            Tokenizer tokenizer = new Tokenizer(expressionString);
            this.tokenStream = tokenizer.process();
            this.tokenStreamLength = this.tokenStream.size();
            this.tokenStreamPointer = 0;
            this.constructedNodes.clear();
            SpelNodeImpl ast = eatExpression();
            if (moreTokens()) {
                throw new SpelParseException(peekToken().startPos, SpelMessage.MORE_INPUT, toString(nextToken()));
            }
            Assert.isTrue(this.constructedNodes.isEmpty(), "At least one node expected");
            return new SpelExpression(expressionString, ast, this.configuration);
        } catch (InternalParseException ex) {
            throw ex.getCause();
        }
    }

    private SpelNodeImpl eatExpression() {
        SpelNodeImpl expr = eatLogicalOrExpression();
        if (moreTokens()) {
            Token t = peekToken();
            if (t.kind == TokenKind.ASSIGN) {
                if (expr == null) {
                    expr = new NullLiteral(toPos(t.startPos - 1, t.endPos - 1));
                }
                nextToken();
                SpelNodeImpl assignedValue = eatLogicalOrExpression();
                return new Assign(toPos(t), expr, assignedValue);
            }
            if (t.kind == TokenKind.ELVIS) {
                if (expr == null) {
                    expr = new NullLiteral(toPos(t.startPos - 1, t.endPos - 2));
                }
                nextToken();
                SpelNodeImpl valueIfNull = eatExpression();
                if (valueIfNull == null) {
                    valueIfNull = new NullLiteral(toPos(t.startPos + 1, t.endPos + 1));
                }
                return new Elvis(toPos(t), expr, valueIfNull);
            }
            if (t.kind == TokenKind.QMARK) {
                if (expr == null) {
                    expr = new NullLiteral(toPos(t.startPos - 1, t.endPos - 1));
                }
                nextToken();
                SpelNodeImpl ifTrueExprValue = eatExpression();
                eatToken(TokenKind.COLON);
                SpelNodeImpl ifFalseExprValue = eatExpression();
                return new Ternary(toPos(t), expr, ifTrueExprValue, ifFalseExprValue);
            }
        }
        return expr;
    }

    private SpelNodeImpl eatLogicalOrExpression() {
        SpelNodeImpl spelNodeImplEatLogicalAndExpression = eatLogicalAndExpression();
        while (true) {
            SpelNodeImpl expr = spelNodeImplEatLogicalAndExpression;
            if (peekIdentifierToken("or") || peekToken(TokenKind.SYMBOLIC_OR)) {
                Token t = nextToken();
                SpelNodeImpl rhExpr = eatLogicalAndExpression();
                checkOperands(t, expr, rhExpr);
                spelNodeImplEatLogicalAndExpression = new OpOr(toPos(t), expr, rhExpr);
            } else {
                return expr;
            }
        }
    }

    private SpelNodeImpl eatLogicalAndExpression() {
        SpelNodeImpl spelNodeImplEatRelationalExpression = eatRelationalExpression();
        while (true) {
            SpelNodeImpl expr = spelNodeImplEatRelationalExpression;
            if (peekIdentifierToken("and") || peekToken(TokenKind.SYMBOLIC_AND)) {
                Token t = nextToken();
                SpelNodeImpl rhExpr = eatRelationalExpression();
                checkOperands(t, expr, rhExpr);
                spelNodeImplEatRelationalExpression = new OpAnd(toPos(t), expr, rhExpr);
            } else {
                return expr;
            }
        }
    }

    private SpelNodeImpl eatRelationalExpression() {
        SpelNodeImpl expr = eatSumExpression();
        Token relationalOperatorToken = maybeEatRelationalOperator();
        if (relationalOperatorToken != null) {
            Token t = nextToken();
            SpelNodeImpl rhExpr = eatSumExpression();
            checkOperands(t, expr, rhExpr);
            TokenKind tk2 = relationalOperatorToken.kind;
            if (relationalOperatorToken.isNumericRelationalOperator()) {
                int pos = toPos(t);
                if (tk2 == TokenKind.GT) {
                    return new OpGT(pos, expr, rhExpr);
                }
                if (tk2 == TokenKind.LT) {
                    return new OpLT(pos, expr, rhExpr);
                }
                if (tk2 == TokenKind.LE) {
                    return new OpLE(pos, expr, rhExpr);
                }
                if (tk2 == TokenKind.GE) {
                    return new OpGE(pos, expr, rhExpr);
                }
                if (tk2 == TokenKind.EQ) {
                    return new OpEQ(pos, expr, rhExpr);
                }
                Assert.isTrue(tk2 == TokenKind.NE, "Not-equals token expected");
                return new OpNE(pos, expr, rhExpr);
            }
            if (tk2 == TokenKind.INSTANCEOF) {
                return new OperatorInstanceof(toPos(t), expr, rhExpr);
            }
            if (tk2 == TokenKind.MATCHES) {
                return new OperatorMatches(toPos(t), expr, rhExpr);
            }
            Assert.isTrue(tk2 == TokenKind.BETWEEN, "Between token expected");
            return new OperatorBetween(toPos(t), expr, rhExpr);
        }
        return expr;
    }

    private SpelNodeImpl eatSumExpression() {
        SpelNodeImpl expr = eatProductExpression();
        while (peekToken(TokenKind.PLUS, TokenKind.MINUS, TokenKind.INC)) {
            Token t = nextToken();
            SpelNodeImpl rhExpr = eatProductExpression();
            checkRightOperand(t, rhExpr);
            if (t.kind == TokenKind.PLUS) {
                expr = new OpPlus(toPos(t), expr, rhExpr);
            } else if (t.kind == TokenKind.MINUS) {
                expr = new OpMinus(toPos(t), expr, rhExpr);
            }
        }
        return expr;
    }

    private SpelNodeImpl eatProductExpression() {
        SpelNodeImpl spelNodeImplEatPowerIncDecExpression = eatPowerIncDecExpression();
        while (true) {
            SpelNodeImpl expr = spelNodeImplEatPowerIncDecExpression;
            if (peekToken(TokenKind.STAR, TokenKind.DIV, TokenKind.MOD)) {
                Token t = nextToken();
                SpelNodeImpl rhExpr = eatPowerIncDecExpression();
                checkOperands(t, expr, rhExpr);
                if (t.kind == TokenKind.STAR) {
                    spelNodeImplEatPowerIncDecExpression = new OpMultiply(toPos(t), expr, rhExpr);
                } else if (t.kind == TokenKind.DIV) {
                    spelNodeImplEatPowerIncDecExpression = new OpDivide(toPos(t), expr, rhExpr);
                } else {
                    Assert.isTrue(t.kind == TokenKind.MOD, "Mod token expected");
                    spelNodeImplEatPowerIncDecExpression = new OpModulus(toPos(t), expr, rhExpr);
                }
            } else {
                return expr;
            }
        }
    }

    private SpelNodeImpl eatPowerIncDecExpression() {
        SpelNodeImpl expr = eatUnaryExpression();
        if (peekToken(TokenKind.POWER)) {
            Token t = nextToken();
            SpelNodeImpl rhExpr = eatUnaryExpression();
            checkRightOperand(t, rhExpr);
            return new OperatorPower(toPos(t), expr, rhExpr);
        }
        if (expr != null && peekToken(TokenKind.INC, TokenKind.DEC)) {
            Token t2 = nextToken();
            if (t2.getKind() == TokenKind.INC) {
                return new OpInc(toPos(t2), true, expr);
            }
            return new OpDec(toPos(t2), true, expr);
        }
        return expr;
    }

    private SpelNodeImpl eatUnaryExpression() {
        if (peekToken(TokenKind.PLUS, TokenKind.MINUS, TokenKind.NOT)) {
            Token t = nextToken();
            SpelNodeImpl expr = eatUnaryExpression();
            if (t.kind == TokenKind.NOT) {
                return new OperatorNot(toPos(t), expr);
            }
            if (t.kind == TokenKind.PLUS) {
                return new OpPlus(toPos(t), expr);
            }
            Assert.isTrue(t.kind == TokenKind.MINUS, "Minus token expected");
            return new OpMinus(toPos(t), expr);
        }
        if (peekToken(TokenKind.INC, TokenKind.DEC)) {
            Token t2 = nextToken();
            SpelNodeImpl expr2 = eatUnaryExpression();
            if (t2.getKind() == TokenKind.INC) {
                return new OpInc(toPos(t2), false, expr2);
            }
            return new OpDec(toPos(t2), false, expr2);
        }
        return eatPrimaryExpression();
    }

    private SpelNodeImpl eatPrimaryExpression() {
        List<SpelNodeImpl> nodes = new ArrayList<>();
        SpelNodeImpl start = eatStartNode();
        nodes.add(start);
        while (maybeEatNode()) {
            nodes.add(pop());
        }
        if (nodes.size() == 1) {
            return nodes.get(0);
        }
        return new CompoundExpression(toPos(start.getStartPosition(), nodes.get(nodes.size() - 1).getEndPosition()), (SpelNodeImpl[]) nodes.toArray(new SpelNodeImpl[nodes.size()]));
    }

    private boolean maybeEatNode() {
        SpelNodeImpl expr;
        if (peekToken(TokenKind.DOT, TokenKind.SAFE_NAVI)) {
            expr = eatDottedNode();
        } else {
            expr = maybeEatNonDottedNode();
        }
        if (expr == null) {
            return false;
        }
        push(expr);
        return true;
    }

    private SpelNodeImpl maybeEatNonDottedNode() {
        if (peekToken(TokenKind.LSQUARE) && maybeEatIndexer()) {
            return pop();
        }
        return null;
    }

    private SpelNodeImpl eatDottedNode() {
        Token t = nextToken();
        boolean nullSafeNavigation = t.kind == TokenKind.SAFE_NAVI;
        if (maybeEatMethodOrProperty(nullSafeNavigation) || maybeEatFunctionOrVar() || maybeEatProjection(nullSafeNavigation) || maybeEatSelection(nullSafeNavigation)) {
            return pop();
        }
        if (peekToken() == null) {
            raiseInternalException(t.startPos, SpelMessage.OOD, new Object[0]);
            return null;
        }
        raiseInternalException(t.startPos, SpelMessage.UNEXPECTED_DATA_AFTER_DOT, toString(peekToken()));
        return null;
    }

    private boolean maybeEatFunctionOrVar() {
        if (!peekToken(TokenKind.HASH)) {
            return false;
        }
        Token t = nextToken();
        Token functionOrVariableName = eatToken(TokenKind.IDENTIFIER);
        SpelNodeImpl[] args = maybeEatMethodArgs();
        if (args == null) {
            push(new VariableReference(functionOrVariableName.data, toPos(t.startPos, functionOrVariableName.endPos)));
            return true;
        }
        push(new FunctionReference(functionOrVariableName.data, toPos(t.startPos, functionOrVariableName.endPos), args));
        return true;
    }

    private SpelNodeImpl[] maybeEatMethodArgs() {
        if (!peekToken(TokenKind.LPAREN)) {
            return null;
        }
        List<SpelNodeImpl> args = new ArrayList<>();
        consumeArguments(args);
        eatToken(TokenKind.RPAREN);
        return (SpelNodeImpl[]) args.toArray(new SpelNodeImpl[args.size()]);
    }

    private void eatConstructorArgs(List<SpelNodeImpl> accumulatedArguments) {
        if (!peekToken(TokenKind.LPAREN)) {
            throw new InternalParseException(new SpelParseException(this.expressionString, positionOf(peekToken()), SpelMessage.MISSING_CONSTRUCTOR_ARGS, new Object[0]));
        }
        consumeArguments(accumulatedArguments);
        eatToken(TokenKind.RPAREN);
    }

    private void consumeArguments(List<SpelNodeImpl> accumulatedArguments) {
        Token next;
        int pos = peekToken().startPos;
        do {
            nextToken();
            Token t = peekToken();
            if (t == null) {
                raiseInternalException(pos, SpelMessage.RUN_OUT_OF_ARGUMENTS, new Object[0]);
            }
            if (t.kind != TokenKind.RPAREN) {
                accumulatedArguments.add(eatExpression());
            }
            next = peekToken();
            if (next == null) {
                break;
            }
        } while (next.kind == TokenKind.COMMA);
        if (next == null) {
            raiseInternalException(pos, SpelMessage.RUN_OUT_OF_ARGUMENTS, new Object[0]);
        }
    }

    private int positionOf(Token t) {
        if (t == null) {
            return this.expressionString.length();
        }
        return t.startPos;
    }

    private SpelNodeImpl eatStartNode() {
        if (maybeEatLiteral()) {
            return pop();
        }
        if (maybeEatParenExpression()) {
            return pop();
        }
        if (maybeEatTypeReference() || maybeEatNullReference() || maybeEatConstructorReference() || maybeEatMethodOrProperty(false) || maybeEatFunctionOrVar()) {
            return pop();
        }
        if (maybeEatBeanReference()) {
            return pop();
        }
        if (maybeEatProjection(false) || maybeEatSelection(false) || maybeEatIndexer()) {
            return pop();
        }
        if (maybeEatInlineListOrMap()) {
            return pop();
        }
        return null;
    }

    private boolean maybeEatBeanReference() {
        BeanReference beanReference;
        if (peekToken(TokenKind.BEAN_REF) || peekToken(TokenKind.FACTORY_BEAN_REF)) {
            Token beanRefToken = nextToken();
            Token beanNameToken = null;
            String beanName = null;
            if (peekToken(TokenKind.IDENTIFIER)) {
                beanNameToken = eatToken(TokenKind.IDENTIFIER);
                beanName = beanNameToken.data;
            } else if (peekToken(TokenKind.LITERAL_STRING)) {
                beanNameToken = eatToken(TokenKind.LITERAL_STRING);
                String beanName2 = beanNameToken.stringValue();
                beanName = beanName2.substring(1, beanName2.length() - 1);
            } else {
                raiseInternalException(beanRefToken.startPos, SpelMessage.INVALID_BEAN_REFERENCE, new Object[0]);
            }
            if (beanRefToken.getKind() == TokenKind.FACTORY_BEAN_REF) {
                String beanNameString = TokenKind.FACTORY_BEAN_REF.tokenChars + beanName;
                beanReference = new BeanReference(toPos(beanRefToken.startPos, beanNameToken.endPos), beanNameString);
            } else {
                beanReference = new BeanReference(toPos(beanNameToken), beanName);
            }
            this.constructedNodes.push(beanReference);
            return true;
        }
        return false;
    }

    private boolean maybeEatTypeReference() {
        if (peekToken(TokenKind.IDENTIFIER)) {
            Token typeName = peekToken();
            if (!"T".equals(typeName.stringValue())) {
                return false;
            }
            Token t = nextToken();
            if (peekToken(TokenKind.RSQUARE)) {
                push(new PropertyOrFieldReference(false, t.data, toPos(t)));
                return true;
            }
            eatToken(TokenKind.LPAREN);
            SpelNodeImpl node = eatPossiblyQualifiedId();
            int dims = 0;
            while (peekToken(TokenKind.LSQUARE, true)) {
                eatToken(TokenKind.RSQUARE);
                dims++;
            }
            eatToken(TokenKind.RPAREN);
            this.constructedNodes.push(new TypeReference(toPos(typeName), node, dims));
            return true;
        }
        return false;
    }

    private boolean maybeEatNullReference() {
        if (peekToken(TokenKind.IDENTIFIER)) {
            Token nullToken = peekToken();
            if (!"null".equalsIgnoreCase(nullToken.stringValue())) {
                return false;
            }
            nextToken();
            this.constructedNodes.push(new NullLiteral(toPos(nullToken)));
            return true;
        }
        return false;
    }

    private boolean maybeEatProjection(boolean nullSafeNavigation) {
        Token t = peekToken();
        if (!peekToken(TokenKind.PROJECT, true)) {
            return false;
        }
        SpelNodeImpl expr = eatExpression();
        eatToken(TokenKind.RSQUARE);
        this.constructedNodes.push(new Projection(nullSafeNavigation, toPos(t), expr));
        return true;
    }

    private boolean maybeEatInlineListOrMap() {
        Token t = peekToken();
        if (!peekToken(TokenKind.LCURLY, true)) {
            return false;
        }
        SpelNodeImpl expr = null;
        Token closingCurly = peekToken();
        if (peekToken(TokenKind.RCURLY, true)) {
            expr = new InlineList(toPos(t.startPos, closingCurly.endPos), new SpelNodeImpl[0]);
        } else if (peekToken(TokenKind.COLON, true)) {
            Token closingCurly2 = eatToken(TokenKind.RCURLY);
            expr = new InlineMap(toPos(t.startPos, closingCurly2.endPos), new SpelNodeImpl[0]);
        } else {
            SpelNodeImpl firstExpression = eatExpression();
            if (peekToken(TokenKind.RCURLY)) {
                List<SpelNodeImpl> listElements = new ArrayList<>();
                listElements.add(firstExpression);
                Token closingCurly3 = eatToken(TokenKind.RCURLY);
                expr = new InlineList(toPos(t.startPos, closingCurly3.endPos), (SpelNodeImpl[]) listElements.toArray(new SpelNodeImpl[listElements.size()]));
            } else if (peekToken(TokenKind.COMMA, true)) {
                List<SpelNodeImpl> listElements2 = new ArrayList<>();
                listElements2.add(firstExpression);
                do {
                    listElements2.add(eatExpression());
                } while (peekToken(TokenKind.COMMA, true));
                Token closingCurly4 = eatToken(TokenKind.RCURLY);
                expr = new InlineList(toPos(t.startPos, closingCurly4.endPos), (SpelNodeImpl[]) listElements2.toArray(new SpelNodeImpl[listElements2.size()]));
            } else if (peekToken(TokenKind.COLON, true)) {
                List<SpelNodeImpl> mapElements = new ArrayList<>();
                mapElements.add(firstExpression);
                mapElements.add(eatExpression());
                while (peekToken(TokenKind.COMMA, true)) {
                    mapElements.add(eatExpression());
                    eatToken(TokenKind.COLON);
                    mapElements.add(eatExpression());
                }
                Token closingCurly5 = eatToken(TokenKind.RCURLY);
                expr = new InlineMap(toPos(t.startPos, closingCurly5.endPos), (SpelNodeImpl[]) mapElements.toArray(new SpelNodeImpl[mapElements.size()]));
            } else {
                raiseInternalException(t.startPos, SpelMessage.OOD, new Object[0]);
            }
        }
        this.constructedNodes.push(expr);
        return true;
    }

    private boolean maybeEatIndexer() {
        Token t = peekToken();
        if (!peekToken(TokenKind.LSQUARE, true)) {
            return false;
        }
        SpelNodeImpl expr = eatExpression();
        eatToken(TokenKind.RSQUARE);
        this.constructedNodes.push(new Indexer(toPos(t), expr));
        return true;
    }

    private boolean maybeEatSelection(boolean nullSafeNavigation) {
        Token t = peekToken();
        if (!peekSelectToken()) {
            return false;
        }
        nextToken();
        SpelNodeImpl expr = eatExpression();
        if (expr == null) {
            raiseInternalException(toPos(t), SpelMessage.MISSING_SELECTION_EXPRESSION, new Object[0]);
        }
        eatToken(TokenKind.RSQUARE);
        if (t.kind == TokenKind.SELECT_FIRST) {
            this.constructedNodes.push(new Selection(nullSafeNavigation, 1, toPos(t), expr));
            return true;
        }
        if (t.kind == TokenKind.SELECT_LAST) {
            this.constructedNodes.push(new Selection(nullSafeNavigation, 2, toPos(t), expr));
            return true;
        }
        this.constructedNodes.push(new Selection(nullSafeNavigation, 0, toPos(t), expr));
        return true;
    }

    private SpelNodeImpl eatPossiblyQualifiedId() {
        Token node;
        LinkedList<SpelNodeImpl> qualifiedIdPieces = new LinkedList<>();
        Token tokenPeekToken = peekToken();
        while (true) {
            node = tokenPeekToken;
            if (!isValidQualifiedId(node)) {
                break;
            }
            nextToken();
            if (node.kind != TokenKind.DOT) {
                qualifiedIdPieces.add(new Identifier(node.stringValue(), toPos(node)));
            }
            tokenPeekToken = peekToken();
        }
        if (qualifiedIdPieces.isEmpty()) {
            if (node == null) {
                raiseInternalException(this.expressionString.length(), SpelMessage.OOD, new Object[0]);
            }
            raiseInternalException(node.startPos, SpelMessage.NOT_EXPECTED_TOKEN, "qualified ID", node.getKind().toString().toLowerCase());
        }
        int pos = toPos(qualifiedIdPieces.getFirst().getStartPosition(), qualifiedIdPieces.getLast().getEndPosition());
        return new QualifiedIdentifier(pos, (SpelNodeImpl[]) qualifiedIdPieces.toArray(new SpelNodeImpl[qualifiedIdPieces.size()]));
    }

    private boolean isValidQualifiedId(Token node) {
        if (node == null || node.kind == TokenKind.LITERAL_STRING) {
            return false;
        }
        if (node.kind == TokenKind.DOT || node.kind == TokenKind.IDENTIFIER) {
            return true;
        }
        String value = node.stringValue();
        return StringUtils.hasLength(value) && VALID_QUALIFIED_ID_PATTERN.matcher(value).matches();
    }

    private boolean maybeEatMethodOrProperty(boolean nullSafeNavigation) {
        if (peekToken(TokenKind.IDENTIFIER)) {
            Token methodOrPropertyName = nextToken();
            SpelNodeImpl[] args = maybeEatMethodArgs();
            if (args == null) {
                push(new PropertyOrFieldReference(nullSafeNavigation, methodOrPropertyName.data, toPos(methodOrPropertyName)));
                return true;
            }
            push(new MethodReference(nullSafeNavigation, methodOrPropertyName.data, toPos(methodOrPropertyName), args));
            return true;
        }
        return false;
    }

    private boolean maybeEatConstructorReference() {
        if (peekIdentifierToken("new")) {
            Token newToken = nextToken();
            if (peekToken(TokenKind.RSQUARE)) {
                push(new PropertyOrFieldReference(false, newToken.data, toPos(newToken)));
                return true;
            }
            SpelNodeImpl possiblyQualifiedConstructorName = eatPossiblyQualifiedId();
            List<SpelNodeImpl> nodes = new ArrayList<>();
            nodes.add(possiblyQualifiedConstructorName);
            if (peekToken(TokenKind.LSQUARE)) {
                List<SpelNodeImpl> dimensions = new ArrayList<>();
                while (peekToken(TokenKind.LSQUARE, true)) {
                    if (!peekToken(TokenKind.RSQUARE)) {
                        dimensions.add(eatExpression());
                    } else {
                        dimensions.add(null);
                    }
                    eatToken(TokenKind.RSQUARE);
                }
                if (maybeEatInlineListOrMap()) {
                    nodes.add(pop());
                }
                push(new ConstructorReference(toPos(newToken), (SpelNodeImpl[]) dimensions.toArray(new SpelNodeImpl[dimensions.size()]), (SpelNodeImpl[]) nodes.toArray(new SpelNodeImpl[nodes.size()])));
                return true;
            }
            eatConstructorArgs(nodes);
            push(new ConstructorReference(toPos(newToken), (SpelNodeImpl[]) nodes.toArray(new SpelNodeImpl[nodes.size()])));
            return true;
        }
        return false;
    }

    private void push(SpelNodeImpl newNode) {
        this.constructedNodes.push(newNode);
    }

    private SpelNodeImpl pop() {
        return this.constructedNodes.pop();
    }

    private boolean maybeEatLiteral() {
        Token t = peekToken();
        if (t == null) {
            return false;
        }
        if (t.kind == TokenKind.LITERAL_INT) {
            push(Literal.getIntLiteral(t.data, toPos(t), 10));
        } else if (t.kind == TokenKind.LITERAL_LONG) {
            push(Literal.getLongLiteral(t.data, toPos(t), 10));
        } else if (t.kind == TokenKind.LITERAL_HEXINT) {
            push(Literal.getIntLiteral(t.data, toPos(t), 16));
        } else if (t.kind == TokenKind.LITERAL_HEXLONG) {
            push(Literal.getLongLiteral(t.data, toPos(t), 16));
        } else if (t.kind == TokenKind.LITERAL_REAL) {
            push(Literal.getRealLiteral(t.data, toPos(t), false));
        } else if (t.kind == TokenKind.LITERAL_REAL_FLOAT) {
            push(Literal.getRealLiteral(t.data, toPos(t), true));
        } else if (peekIdentifierToken("true")) {
            push(new BooleanLiteral(t.data, toPos(t), true));
        } else if (peekIdentifierToken("false")) {
            push(new BooleanLiteral(t.data, toPos(t), false));
        } else if (t.kind == TokenKind.LITERAL_STRING) {
            push(new StringLiteral(t.data, toPos(t), t.data));
        } else {
            return false;
        }
        nextToken();
        return true;
    }

    private boolean maybeEatParenExpression() {
        if (peekToken(TokenKind.LPAREN)) {
            nextToken();
            SpelNodeImpl expr = eatExpression();
            eatToken(TokenKind.RPAREN);
            push(expr);
            return true;
        }
        return false;
    }

    private Token maybeEatRelationalOperator() {
        Token t = peekToken();
        if (t == null) {
            return null;
        }
        if (t.isNumericRelationalOperator()) {
            return t;
        }
        if (t.isIdentifier()) {
            String idString = t.stringValue();
            if (idString.equalsIgnoreCase("instanceof")) {
                return t.asInstanceOfToken();
            }
            if (idString.equalsIgnoreCase("matches")) {
                return t.asMatchesToken();
            }
            if (idString.equalsIgnoreCase("between")) {
                return t.asBetweenToken();
            }
            return null;
        }
        return null;
    }

    private Token eatToken(TokenKind expectedKind) {
        Token t = nextToken();
        if (t == null) {
            raiseInternalException(this.expressionString.length(), SpelMessage.OOD, new Object[0]);
        }
        if (t.kind != expectedKind) {
            raiseInternalException(t.startPos, SpelMessage.NOT_EXPECTED_TOKEN, expectedKind.toString().toLowerCase(), t.getKind().toString().toLowerCase());
        }
        return t;
    }

    private boolean peekToken(TokenKind desiredTokenKind) {
        return peekToken(desiredTokenKind, false);
    }

    private boolean peekToken(TokenKind desiredTokenKind, boolean consumeIfMatched) {
        if (!moreTokens()) {
            return false;
        }
        Token t = peekToken();
        if (t.kind == desiredTokenKind) {
            if (consumeIfMatched) {
                this.tokenStreamPointer++;
                return true;
            }
            return true;
        }
        if (desiredTokenKind == TokenKind.IDENTIFIER && t.kind.ordinal() >= TokenKind.DIV.ordinal() && t.kind.ordinal() <= TokenKind.NOT.ordinal() && t.data != null) {
            return true;
        }
        return false;
    }

    private boolean peekToken(TokenKind possible1, TokenKind possible2) {
        if (!moreTokens()) {
            return false;
        }
        Token t = peekToken();
        return t.kind == possible1 || t.kind == possible2;
    }

    private boolean peekToken(TokenKind possible1, TokenKind possible2, TokenKind possible3) {
        if (!moreTokens()) {
            return false;
        }
        Token t = peekToken();
        return t.kind == possible1 || t.kind == possible2 || t.kind == possible3;
    }

    private boolean peekIdentifierToken(String identifierString) {
        if (!moreTokens()) {
            return false;
        }
        Token t = peekToken();
        return t.kind == TokenKind.IDENTIFIER && t.stringValue().equalsIgnoreCase(identifierString);
    }

    private boolean peekSelectToken() {
        if (!moreTokens()) {
            return false;
        }
        Token t = peekToken();
        return t.kind == TokenKind.SELECT || t.kind == TokenKind.SELECT_FIRST || t.kind == TokenKind.SELECT_LAST;
    }

    private boolean moreTokens() {
        return this.tokenStreamPointer < this.tokenStream.size();
    }

    private Token nextToken() {
        if (this.tokenStreamPointer >= this.tokenStreamLength) {
            return null;
        }
        List<Token> list = this.tokenStream;
        int i = this.tokenStreamPointer;
        this.tokenStreamPointer = i + 1;
        return list.get(i);
    }

    private Token peekToken() {
        if (this.tokenStreamPointer >= this.tokenStreamLength) {
            return null;
        }
        return this.tokenStream.get(this.tokenStreamPointer);
    }

    private void raiseInternalException(int pos, SpelMessage message, Object... inserts) {
        throw new InternalParseException(new SpelParseException(this.expressionString, pos, message, inserts));
    }

    public String toString(Token t) {
        if (t.getKind().hasPayload()) {
            return t.stringValue();
        }
        return t.kind.toString().toLowerCase();
    }

    private void checkOperands(Token token, SpelNodeImpl left, SpelNodeImpl right) {
        checkLeftOperand(token, left);
        checkRightOperand(token, right);
    }

    private void checkLeftOperand(Token token, SpelNodeImpl operandExpression) {
        if (operandExpression == null) {
            raiseInternalException(token.startPos, SpelMessage.LEFT_OPERAND_PROBLEM, new Object[0]);
        }
    }

    private void checkRightOperand(Token token, SpelNodeImpl operandExpression) {
        if (operandExpression == null) {
            raiseInternalException(token.startPos, SpelMessage.RIGHT_OPERAND_PROBLEM, new Object[0]);
        }
    }

    private int toPos(Token t) {
        return (t.startPos << 16) + t.endPos;
    }

    private int toPos(int start, int end) {
        return (start << 16) + end;
    }
}
