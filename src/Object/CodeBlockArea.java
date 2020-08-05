package Object;

import javafx.application.Platform;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import utility.CodeUtils;

import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CodeBlockArea extends CodeArea {
    public CodeBlockArea() {
        this.getStyleClass().add("code-area");
        this.setParagraphGraphicFactory(LineNumberFactory.get(this));

        this.multiPlainChanges()
                .successionEnds(Duration.ofMillis(500))
                .subscribe(ignore -> this.setStyleSpans(0, CodeUtils.computeHighlighting(this.getText())));

        final Pattern whiteSpace = Pattern.compile("^\\s+");
        this.addEventHandler(KeyEvent.KEY_PRESSED, KE ->
        {
            if (KE.getCode() == KeyCode.ENTER) {
                int caretPosition = this.getCaretPosition();
                int currentParagraph = this.getCurrentParagraph();
                Matcher m0 = whiteSpace.matcher(this.getParagraph(currentParagraph - 1).getSegments().get(0));
                if (m0.find()) Platform.runLater(() -> this.insertText(caretPosition, m0.group()));
            }
        });

        this.replaceText(CodeUtils.getSampleCode());
    }

    public void setAreaPrefWidth(double input){
        this.setPrefWidth(input);
    }

    public void setAreaPrefHeight(double input){
        this.setPrefHeight(input);
    }
}
