package com.jetbrains.teamcity.command;

import java.text.MessageFormat;
import java.util.Collection;

import javax.naming.directory.InvalidAttributesException;

import jetbrains.buildServer.vcs.VcsRoot;

import com.jetbrains.teamcity.EAuthorizationException;
import com.jetbrains.teamcity.ECommunicationException;
import com.jetbrains.teamcity.ERemoteError;
import com.jetbrains.teamcity.Server;
import com.jetbrains.teamcity.Util;
import com.jetbrains.teamcity.command.ICommand;
import com.jetbrains.teamcity.resources.IShare;
import com.jetbrains.teamcity.resources.TCAccess;

public class Share implements ICommand {
	
	private static final String ID = "share";

	private static final String INFO_PARAM = "-i";
	private static final String INFO_PARAM_LONG = "--info";
	private static final String LOCAL_PARAM = "-l";
	private static final String LOCAL_PARAM_LONG = "--local";
	private static final String VCSROOT_PARAM = "-v";
	private static final String VCSROOT_PARAM_LONG = "--vcsroot";

	public void execute(final Server server, String[] args) throws EAuthorizationException, ECommunicationException, ERemoteError, InvalidAttributesException {
		
		if (Util.hasArgument(args, VCSROOT_PARAM, VCSROOT_PARAM_LONG) && Util.hasArgument(args, LOCAL_PARAM, LOCAL_PARAM_LONG)) {
			final String localPath = Util.getArgumentValue(args, LOCAL_PARAM, LOCAL_PARAM_LONG);
			final String vcsRootId = Util.getArgumentValue(args, VCSROOT_PARAM, VCSROOT_PARAM_LONG);
			if(vcsRootId != null){
				//check format
				final long id;
				try{
					id = new Long(vcsRootId).longValue();
				} catch (NumberFormatException e){
					throw new IllegalArgumentException(MessageFormat.format("wrong Id format: {0}", vcsRootId), e);
				}
				//try to find root
				for(final VcsRoot root : server.getVcsRoots()){
					if (id == root.getId()) {
						if(localPath == null){
							throw new IllegalArgumentException("no local path passed");
						}
						final String shareId = share(localPath, root).getId();
						System.out.println(MessageFormat.format("{0}", shareId));
						return;
					}
				}
				throw new IllegalArgumentException(MessageFormat.format("no VcsRoot found. id={0}", vcsRootId));
			}
			return;
		} else if (Util.hasArgument(args, INFO_PARAM, INFO_PARAM_LONG)){
			final Collection<IShare> roots = TCAccess.getInstance().roots();
			if(roots.isEmpty()){
				System.out.println("no one share found");
				return;
			}
			System.out.println("id\tlocal\tremote\tproperties");
			for(final IShare root : roots){
				System.out.println(MessageFormat.format("{0}\t{1}\t{2}\t{3}", root.getId(),  root.getLocal(), String.valueOf(root.getRemote()), root.getProperties()));
			}
			return;
		}
		System.out.println(getUsageDescription());
	}

	private IShare share(final String localPath, final VcsRoot root) {
		return TCAccess.getInstance().share(localPath, root);
	}

	public String getId() {
		return ID;
	}

	public boolean isConnectionRequired(final String[] args) {
		return Util.hasArgument(args, VCSROOT_PARAM, VCSROOT_PARAM_LONG) && Util.hasArgument(args, LOCAL_PARAM, LOCAL_PARAM_LONG);
	}

	public String getUsageDescription() {
		return MessageFormat.format("{0}: use -v [vcs_root_id] -l [local_path]", getId()); 
	}
	
	public String getDescription() {
		return "Associates local folder with known TeamCity VcsRoot";
	}
	

}
