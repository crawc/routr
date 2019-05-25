/**
 * @author Pedro Sanders
 * @since v1
 */
package com.fonoster.routr.core;

import org.graalvm.polyglot.Context;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class Launcher {
    private static String launchScript = "var System = Java.type('java.lang.System');"
      + "load(System.getProperty('user.dir') + '/libs/app.bundle.js')";

    static public void main(String... args) {
        new Launcher().launch();
    }

    // TODO: Check Java version and show warning if version >= 11
    public void launch() {
        String engine = System.getenv("ROUTR_JS_ENGINE");

        if (engine != null && engine.equals("graal.js")) {
          launchWithGraalJS();
        } else if (engine != null && engine.equals("nashorn")) {
          launchWithNashorn();
        } else {
          launchWithNashorn();
        }
    }

    public void launchWithNashorn() {
        try {
            ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
            engine.put("timer", new Timer());
            engine.eval(launchScript);
        } catch(ScriptException ex) {
            System.out.println("Unable to run with 'nashorn' engine: " + ex.getMessage());
        }
    }

    public void launchWithGraalJS() {
        Context polyglot = Context
          .newBuilder()
          .option("js.nashorn-compat", "true")
          .allowExperimentalOptions(true)
          .allowIO(true)
          .allowAllAccess(true).build();

          polyglot.getBindings("js").putMember("timer", new Timer());
          polyglot.eval("js", launchScript);
    }
}
