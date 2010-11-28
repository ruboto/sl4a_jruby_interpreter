/*
 * This file was modified from a code in the SL4A project:
 *    http://code.google.com/p/android-scripting/
 *
 * The modifications are designed to move maintenance for 
 * JRuby's interpreter for SL4A over to the Rubot Community.
 *
 * The original license follows:
 *
 * Copyright (C) 2009 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package org.ruboto.sl4a;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;

import com.googlecode.android_scripting.interpreter.InterpreterUtils;
import com.googlecode.android_scripting.interpreter.InterpreterConstants;
import com.googlecode.android_scripting.interpreter.InterpreterDescriptor;

public class JRubyDescriptor implements InterpreterDescriptor {

  private static final String JRUBY_PREFIX =
      "-e $LOAD_PATH.push('file:%1$s!/META-INF/jruby.home/lib/ruby/1.8'); require 'android'; %2$s";
  private static final String JRUBY_JAR = "jruby.jar";
  private static final String ENV_DATA = "ANDROID_DATA";

  public static final String BASE_INSTALL_URL = "https://github.com/ruboto/sl4a_jruby_interpreter/raw/master/jruby-version-builder/current/";
  public static final String DALVIKVM = "/system/bin/dalvikvm";

  public String getExtension() {
    return ".rb";
  }

  public String getName() {
    return "jruby";
  }

  public String getNiceName() {
    return "JRuby";
  }

  public boolean hasInterpreterArchive() {
    return false;
  }

  public boolean hasExtrasArchive() {
    return true;
  }

  public boolean hasScriptsArchive() {
    return true;
  }

  public boolean hasInteractiveMode() {
    return true;
  }

  public int getVersion() {
    return 3;
  }

  public int getInterpreterVersion() {
    return getVersion();
  }

  public int getExtrasVersion() {
    return getVersion();
  }

  public int getScriptsVersion() {
    return 1;
  }

  public String getInterpreterArchiveName() {
    return String.format("%s.zip", getName());
  }

  public String getExtrasArchiveName() {
    return String.format("%s_extras.zip", getName());
  }

  public String getScriptsArchiveName() {
    return String.format("%s_scripts.zip", getName());
  }

  public String getInterpreterArchiveUrl() {
    return BASE_INSTALL_URL + getInterpreterArchiveName();
  }

  public String getExtrasArchiveUrl() {
    return BASE_INSTALL_URL + getExtrasArchiveName();
  }

  public String getScriptsArchiveUrl() {
    return BASE_INSTALL_URL + getScriptsArchiveName();
  }

  public File getBinary(Context context) {
    return new File(DALVIKVM);
  }

  public String getInteractiveCommand(Context context) {
    String absolutePathToJar = new File(getExtrasPath(context), JRUBY_JAR).getAbsolutePath();
    return String.format(JRUBY_PREFIX, absolutePathToJar,
        "require 'irb'; IRB.conf[:USE_READLINE] = false; IRB.start");
  }

  public String getScriptCommand(Context context) {
    String absolutePathToJar = new File(getExtrasPath(context), JRUBY_JAR).getAbsolutePath();
    return String.format(JRUBY_PREFIX, absolutePathToJar, "load('%s')");
  }

  public List<String> getArguments(Context context) {
    String absolutePathToJar = new File(getExtrasPath(context), JRUBY_JAR).getAbsolutePath();
    return Arrays.asList("-Xbootclasspath:/system/framework/core.jar", "-Xss128k", "-classpath",
        absolutePathToJar, "org.jruby.Main", "-X-C");
  }

  public Map<String, String> getEnvironmentVariables(Context unused) {
    Map<String, String> values = new HashMap<String, String>(1);
    values.put(ENV_DATA, InterpreterConstants.SDCARD_ROOT + getClass().getPackage().getName());
    return values;
  }

  public File getExtrasPath(Context context) {
    if (!hasInterpreterArchive() && hasExtrasArchive()) {
      return new File(InterpreterConstants.SDCARD_ROOT + this.getClass().getPackage().getName()
          + InterpreterConstants.INTERPRETER_EXTRAS_ROOT, getName());
    }
    return InterpreterUtils.getInterpreterRoot(context, getName());
  }
}
