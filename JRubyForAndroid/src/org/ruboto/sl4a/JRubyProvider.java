package org.ruboto.sl4a;

import com.googlecode.android_scripting.interpreter.InterpreterDescriptor;
import com.googlecode.android_scripting.interpreter.InterpreterProvider;

public class JRubyProvider extends InterpreterProvider {
  @Override
  protected InterpreterDescriptor getDescriptor() {
    return new JRubyDescriptor();
  }
}
