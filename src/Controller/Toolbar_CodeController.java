package Controller;

import InsertObj.BasicNode;
import InsertObj.CodeBlock;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import javax.tools.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

public class Toolbar_CodeController {
    private static Toolbar_CodeController instance;
    private PaperController paper_controller = PaperController.getInstance();
    private boolean noSet;

    public static Toolbar_CodeController getInstance() {
        if (instance == null) {
            instance = new Toolbar_CodeController();
        }
        return instance;
    }

    public static void setInstance(Toolbar_CodeController newInstance) {
        instance = newInstance;
    }

    @FXML
    VBox compile;
    @FXML
    TextArea code_result_text;
    @FXML
    CheckBox check_ans;

    @FXML
    public void initialize() {
        setInstance(this);

        check_ans.selectedProperty().addListener((obs, oldValue, newValue) -> {
            BasicNode obj = paper_controller.getFocusObject();
            if(obj != null && obj.getNodeType().equals("code") && !noSet){
                CodeBlock current = ((CodeBlock) obj);
                current.setShowResult(newValue);
            }
        });

        compile.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            BasicNode obj = paper_controller.getFocusObject();
            if(obj != null && obj.getNodeType().equals("code")){
                code_result_text.setText("");

                String code = ((CodeBlock) obj).getCodeString();
                String[] spl = code.split("\\W+");
                String className = "";
                for(int a = 0; a < spl.length ; a++){
                    if(spl[a].equals("class")){
                        className  = spl[a+1];
                        break;
                    }
                }

                File folder = new File("./src/data");
                File sourceFile = new File(folder, className+".java");

                try {
                    Files.write(sourceFile.toPath(), code.getBytes(StandardCharsets.UTF_8));
                } catch (IOException e) {
                    if(code_result_text.getText().length() == 0)
                        code_result_text.setText("-------------------------------MESSAGE--------------------------------");
                    code_result_text.setText(code_result_text.getText() + "\n" + e.toString());
                }

                try {
                    JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
                    if (compiler == null){
                        if(code_result_text.getText().length() == 0)
                            code_result_text.setText("-------------------------------MESSAGE--------------------------------");
                        code_result_text.setText(code_result_text.getText() + "\n" + "ERROR: JDK required (running inside of JRE)");
                    } else {
                        DiagnosticCollector<JavaFileObject> diagnosticsCollector = new DiagnosticCollector<>();
                        StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnosticsCollector, null, null);
                        Iterable<? extends JavaFileObject> compilationUnits = fileManager.getJavaFileObjectsFromStrings(Arrays.asList(sourceFile.getPath()));
                        JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, diagnosticsCollector, null, null, compilationUnits);
                        boolean success = task.call();
                        if (!success) {
                            List<Diagnostic<? extends JavaFileObject>> diagnostics = diagnosticsCollector.getDiagnostics();
                            for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics) {
                                // read error dertails from the diagnostic object
                                if(code_result_text.getText().length() == 0)
                                    code_result_text.setText("-------------------------------MESSAGE--------------------------------");
                                code_result_text.setText(code_result_text.getText() + "\n" + diagnostic.getMessage(null));
                            }
                        } else {
                            ProcessBuilder pb = new ProcessBuilder("java", className);
                            pb.directory(new File("src/data/"));
                            pb.redirectError();
                            Process p = pb.start();
                            InputStreamConsumer inputStream = new InputStreamConsumer(p.getInputStream());
                            inputStream.consume();

                            try{
                                p.waitFor();

                                if(code_result_text.getText().length() == 0)
                                    code_result_text.setText("-------------------------------OUTPUT--------------------------------\n");
                                else
                                    code_result_text.setText(code_result_text.getText() + "-----------------------OUTPUT------------------------\n");
                                code_result_text.setText(code_result_text.getText() + inputStream.getAns());

                                ((CodeBlock) obj).setClassName(className);
                                ((CodeBlock) obj).getResultBlock().setText(inputStream.getAns());
                            } catch(Exception e){
                                if(code_result_text.getText().length() == 0)
                                    code_result_text.setText("-------------------------------MESSAGE--------------------------------");
                                code_result_text.setText(code_result_text.getText() + "\n" + e.toString());
                            }
                        }
                        fileManager.close();
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void setNoSet(boolean noSet) {
        this.noSet = noSet;
    }

    public void setShowResult(boolean input){
        check_ans.setSelected(input);
    }

    public static class InputStreamConsumer implements Runnable {
        private InputStream is;
        private String ans;

        public InputStreamConsumer(InputStream is) {
            this.is = is;
            ans = "";
        }

        public InputStream getInputStream() {
            return is;
        }

        public void consume() {
            Thread t = new Thread(this);
            t.start();
        }

        @Override
        public void run() {
            InputStream is = getInputStream();
            int in = -1;
            try {
                while ((in = is.read()) != -1) {
                    ans = ans + (char)in;
                }
            } catch (IOException exp) {
                exp.printStackTrace();
            }
        }

        public String getAns() {
            return ans;
        }
    }
}

