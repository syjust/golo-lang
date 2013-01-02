package fr.insalyon.citi.golo.runtime;

import org.testng.annotations.Test;

import java.lang.invoke.CallSite;
import java.lang.invoke.MethodHandle;
import java.util.Arrays;
import java.util.List;

import static java.lang.invoke.MethodHandles.lookup;
import static java.lang.invoke.MethodType.genericMethodType;
import static java.lang.invoke.MethodType.methodType;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;


public class ClosureReferenceSupportTest {

  static Object to_list(Object foo, Object bar) {
    return Arrays.asList(foo, bar);
  }

  @Test
  public void check_bootstrap() throws Throwable {
    CallSite callSite = ClosureReferenceSupport.bootstrap(lookup(), "to_list", methodType(MethodHandle.class), 2);
    assertThat(callSite.type(), is(methodType(MethodHandle.class)));

    Object result = callSite.dynamicInvoker().invoke();
    assertThat(result, instanceOf(MethodHandle.class));

    MethodHandle handle = (MethodHandle) result;
    assertThat(handle.type(), is(methodType(Object.class, Object.class, Object.class)));

    result = handle.invoke("foo", "bar");
    assertThat(result, instanceOf(List.class));
    assertThat(((List) result).size(), is(2));
  }
}