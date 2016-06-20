package com.error22.jenkinsci.plugins.pipelinebitbucketserversteps;

import org.apache.commons.lang.StringUtils;
import org.jenkinsci.plugins.workflow.steps.AbstractStepDescriptorImpl;
import org.jenkinsci.plugins.workflow.steps.AbstractStepImpl;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;

import hudson.Extension;
import hudson.model.Descriptor;

public class SetCommitStatusStep extends AbstractStepImpl {
	private String serverUrl, credentials, commitId, state, key, url, name, description;

	@DataBoundConstructor
	public SetCommitStatusStep(String serverUrl, String credentials, String commitId, String state, String key, String url)
			throws Descriptor.FormException {
		this.serverUrl = serverUrl;
		this.credentials = credentials;
		this.commitId = commitId;
		this.state = state;
		this.key = key;
		this.url = url;

		if (StringUtils.isBlank(serverUrl)) {
			throw new Descriptor.FormException("Can not be empty", "serverUrl");
		}
		if (StringUtils.isBlank(credentials)) {
			throw new Descriptor.FormException("Can not be empty", "credentials");
		}
		if (StringUtils.isBlank(commitId)) {
			throw new Descriptor.FormException("Can not be empty", "commitId");
		}
		if (StringUtils.isBlank(state)) {
			throw new Descriptor.FormException("Can not be empty", "state");
		}
		if (StringUtils.isBlank(key)) {
			throw new Descriptor.FormException("Can not be empty", "key");
		}
		if (StringUtils.isBlank(url)) {
			throw new Descriptor.FormException("Can not be empty", "url");
		}
	}

	public String getServerUrl() {
		return serverUrl;
	}

	public String getCredentials() {
		return credentials;
	}

	public String getCommitId() {
		return commitId;
	}

	public String getState() {
		return state;
	}

	public String getKey() {
		return key;
	}

	public String getUrl() {
		return url;
	}

	public String getName() {
		return name;
	}

	@DataBoundSetter
	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	@DataBoundSetter
	public void setDescription(String description) {
		this.description = description;
	}

	@Extension
	public static class DescriptorImpl extends AbstractStepDescriptorImpl {

		public DescriptorImpl() {
			super(SetCommitStatusStepExecution.class);
		}

		@Override
		public String getFunctionName() {
			return "setBBSStatus";
		}

		@Override
		public String getDisplayName() {
			return "Set Bitbucket Commit Build Status";
		}
	}

}
