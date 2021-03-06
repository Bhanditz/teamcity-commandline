package com.jetbrains.teamcity.command;

import java.util.Arrays;
import org.junit.Test;

import static org.junit.Assert.*;


public class ArgsTest {

  @Test
  public void HOST_ARG() throws Exception {
    {
      final String command = "run  -m  --host ";
      final Args args = new Args(command);
      boolean host = args.hasArgument(CommandRunner.HOST_ARG);
      assertFalse(host);
    }
    {
      String host = "http://127.0.0.1:8111";
      final String command = String.format("run  -m  %s    %s   ", CommandRunner.HOST_ARG, host);
      final Args args = new Args(command);
      assertTrue(args.hasArgument(CommandRunner.HOST_ARG));
      assertEquals(host, args.getArgument(CommandRunner.HOST_ARG));
    }
    {
      String host = "https://127.0.0.1:8111";
      final String command = String.format("run  -m  %s    %s   ", CommandRunner.HOST_ARG, host);
      final Args args = new Args(command);
      assertTrue(args.hasArgument(CommandRunner.HOST_ARG));
      assertEquals(host, args.getArgument(CommandRunner.HOST_ARG));
    }

  }


  @Test
  public void USER_ARG() throws Exception {
    {
      final String command = String.format("run   %s  ", CommandRunner.USER_ARG);
      final Args args = new Args(command);
      boolean host = args.hasArgument(CommandRunner.USER_ARG);
      assertFalse(host);
    }
    {
      final String user = "xzxzxzx";
      final String command = String.format("run   %s    %s ", CommandRunner.USER_ARG, user);
      final Args args = new Args(command);
      boolean host = args.hasArgument(CommandRunner.USER_ARG);
      assertTrue(host);
      assertEquals(user, args.getArgument(CommandRunner.USER_ARG));
    }

  }

  @Test
  public void PASSWORD_ARG() throws Exception {
    {
      final String command = String.format("run   %s  ", CommandRunner.PASSWORD_ARG);
      final Args args = new Args(command);
      boolean host = args.hasArgument(CommandRunner.PASSWORD_ARG);
      assertFalse(host);
    }
    {
      final String user = "xzxzxzx";
      final String command = String.format("run   %s    %s ", CommandRunner.PASSWORD_ARG, user);
      final Args args = new Args(command);
      boolean host = args.hasArgument(CommandRunner.PASSWORD_ARG);
      assertTrue(host);
      assertEquals(user, args.getArgument(CommandRunner.PASSWORD_ARG));
    }

  }

  @Test
  public void USER_PASSWORD_ARG() throws Exception {
    {
      final String command = String.format("run   %s  ", CommandRunner.PASSWORD_ARG);
      final Args args = new Args(command);
      boolean host = args.hasArgument(CommandRunner.USER_ARG, CommandRunner.PASSWORD_ARG);
      assertFalse(host);
    }
    {
      final String user = "xzxzxzx";
      final String command = String.format("run   %s    %s ", CommandRunner.PASSWORD_ARG, user);
      final Args args = new Args(command);
      boolean host = args.hasArgument(CommandRunner.USER_ARG, CommandRunner.PASSWORD_ARG);
      assertTrue(host);
    }

  }

  @Test
  public void MESSAGE_ARG() throws Exception {
    {
      final String command = String.format("run   %s   ", RemoteRun.MESSAGE_PARAM);
      final Args args = new Args(command);
      boolean value = args.hasArgument(RemoteRun.MESSAGE_PARAM);
      assertFalse(value);
    }
    {
      final String command = String.format("run   %s \t \n ", RemoteRun.MESSAGE_PARAM);
      final Args args = new Args(command);
      boolean value = args.hasArgument(RemoteRun.MESSAGE_PARAM);
      assertFalse(value);
    }

    {
      final String message = "xzxzxzx";
      final String command = String.format("run   %s    %s ", RemoteRun.MESSAGE_PARAM, message);
      final Args args = new Args(command);
      boolean value = args.hasArgument(RemoteRun.MESSAGE_PARAM);
      assertTrue(value);
    }

  }

  @Test
  public void CONFIGURATION_ARG() throws Exception {
    {
      final String command = String.format("run   %s   ", RemoteRun.CONFIGURATION_PARAM_LONG);
      final Args args = new Args(command);
      boolean value = args.hasArgument(RemoteRun.CONFIGURATION_PARAM_LONG);
      assertFalse(value);
    }
    {
      final String message = "";
      final String command = String.format("run   %s    %s ", RemoteRun.CONFIGURATION_PARAM_LONG, message);
      final Args args = new Args(command);
      boolean value = args.hasArgument(RemoteRun.CONFIGURATION_PARAM_LONG);
      assertFalse(value);
    }
    {
      final String message = "x";
      final String command = String.format("run   %s    %s ", RemoteRun.CONFIGURATION_PARAM_LONG, message);
      final Args args = new Args(command);
      boolean value = args.hasArgument(RemoteRun.CONFIGURATION_PARAM_LONG);
      assertTrue(value);
    }
    {
      final String message = "xzxzxzx";
      final String command = String.format("run   %s    %s ", RemoteRun.CONFIGURATION_PARAM_LONG, message);
      final Args args = new Args(command);
      boolean value = args.hasArgument(RemoteRun.CONFIGURATION_PARAM_LONG);
      assertTrue(value);
    }


  }


  @Test
  public void TIMEOUT_ARG() throws Exception {
    {
      final String command = String.format("run   %s   ", RemoteRun.TIMEOUT_PARAM);
      final Args args = new Args(command);
      boolean value = args.hasArgument(RemoteRun.TIMEOUT_PARAM);
      assertFalse(value);
    }
    {
      final String message = "";
      final String command = String.format("run   %s    %s ", RemoteRun.TIMEOUT_PARAM, message);
      final Args args = new Args(command);
      boolean value = args.hasArgument(RemoteRun.TIMEOUT_PARAM);
      assertFalse(value);
    }
    {
      final String message = "a";
      final String command = String.format("run   %s    %s ", RemoteRun.TIMEOUT_PARAM, message);
      final Args args = new Args(command);
      boolean value = args.hasArgument(RemoteRun.TIMEOUT_PARAM);
      assertFalse(value);
    }
    {
      final String message = "1";
      final String command = String.format("run   %s    %s ", RemoteRun.TIMEOUT_PARAM, message);
      final Args args = new Args(command);
      boolean value = args.hasArgument(RemoteRun.TIMEOUT_PARAM);
      assertTrue(value);
    }
    {
      final String message = "11212";
      final String command = String.format("run   %s    %s ", RemoteRun.TIMEOUT_PARAM, message);
      final Args args = new Args(command);
      boolean value = args.hasArgument(RemoteRun.TIMEOUT_PARAM);
      assertTrue(value);
    }


  }


  @Test
  public void OVERRIDING_MAPPING_FILE_ARG() throws Exception {
    {
      final String command = String.format("run   %s   ", RemoteRun.OVERRIDING_MAPPING_FILE_PARAM);
      final Args args = new Args(command);
      boolean value = args.hasArgument(RemoteRun.OVERRIDING_MAPPING_FILE_PARAM);
      assertFalse(value);
    }
    {
      final String message = "";
      final String command = String.format("run   %s    %s ", RemoteRun.OVERRIDING_MAPPING_FILE_PARAM, message);
      final Args args = new Args(command);
      boolean value = args.hasArgument(RemoteRun.OVERRIDING_MAPPING_FILE_PARAM);
      assertFalse(value);
    }
    {
      final String message = "1111";
      final String command = String.format("run   %s    %s ", RemoteRun.OVERRIDING_MAPPING_FILE_PARAM, message);
      final Args args = new Args(command);
      boolean value = args.hasArgument(RemoteRun.OVERRIDING_MAPPING_FILE_PARAM);
      assertTrue(value);
    }

  }

  @Test
  public void getArgValues() {

    assertEquals(Arrays.asList("foo=bar", "s=d"), new Args("run --param foo=bar --param s=d").getArgValues(RemoteRun.BUILD_PARAM_SWITCH));
    assertEquals(Arrays.asList("foo=bar"), new Args("run --param foo=bar --param").getArgValues("--param"));
    assertEquals(Arrays.asList(), new Args("run --param2 foo=bar --param2").getArgValues("--param"));
    
  }

  @Test
  public void PROJECT_ARG() throws Exception {
    testProjectParameter(RemoteRun.PROJECT_PARAM);
    testProjectParameter(RemoteRun.PROJECT_PARAM_LONG);
  }

  private void testProjectParameter(final String projectParam) {
    {
      final String command = String.format("info  %s", projectParam);
      final Args args = new Args(command);
      assertTrue(args.hasArgument(projectParam));
      assertNull(args.getArgument(projectParam));
    }
    {
      final String command = String.format("info  %s xxxd", projectParam);
      final Args args = new Args(command);
      assertTrue(args.hasArgument(projectParam));
      assertEquals("xxxd", args.getArgument(projectParam));
    }
    {
      final String command = String.format("info  %s xxxd --otherparam eee", projectParam);
      final Args args = new Args(command);
      assertTrue(args.hasArgument(projectParam));
      assertEquals("xxxd", args.getArgument(projectParam));
    }
  }

}
