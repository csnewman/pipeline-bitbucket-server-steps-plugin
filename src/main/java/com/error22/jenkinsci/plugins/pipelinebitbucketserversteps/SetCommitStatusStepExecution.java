package com.error22.jenkinsci.plugins.pipelinebitbucketserversteps;

import javax.inject.Inject;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jenkinsci.plugins.workflow.steps.AbstractSynchronousNonBlockingStepExecution;
import org.jenkinsci.plugins.workflow.steps.StepContextParameter;

import hudson.FilePath;
import hudson.Launcher;
import hudson.model.Run;
import hudson.model.TaskListener;
import net.sf.json.JSONObject;

public class SetCommitStatusStepExecution extends AbstractSynchronousNonBlockingStepExecution<Void> {
	private static final long serialVersionUID = 6035061564412071584L;

	@StepContextParameter
	private transient TaskListener listener;

	@StepContextParameter
	private transient FilePath ws;

	@StepContextParameter
	private transient Run build;

	@StepContextParameter
	private transient Launcher launcher;

	@Inject
	private transient SetCommitStatusStep step;

	@Override
	protected Void run() throws Exception {
		CloseableHttpClient httpclient = HttpClients.createDefault();

		JSONObject obj = new JSONObject();
		obj.put("state", step.getState());
		obj.put("key", step.getKey());
		obj.put("url", step.getUrl());
		if(!StringUtils.isBlank(step.getName()))
			obj.put("name", step.getName());
		if(!StringUtils.isBlank(step.getDescription()))
			obj.put("description", step.getDescription());

		StringEntity requestEntity = new StringEntity(obj.toString(), ContentType.APPLICATION_JSON);

		String url = step.getServerUrl();
		if (!url.endsWith("/"))
			url += "/";
		url += "rest/build-status/1.0/commits/";
		url += step.getCommitId();
		
		listener.getLogger().println("Posting build status to: "+url);

		HttpPost postMethod = new HttpPost(url);
		postMethod.setEntity(requestEntity);
		postMethod.addHeader("Authorization", "Basic "+Base64.encodeBase64String(step.getCredentials().getBytes("UTF8")));
		HttpResponse rawResponse = httpclient.execute(postMethod);
		
		if (rawResponse.getStatusLine().getStatusCode() != 204) {
			listener.getLogger().println("Failed to set build status!");
			listener.getLogger().println("Request: "+obj.toString());
			listener.getLogger().println("Got response code: " +rawResponse.getStatusLine().getStatusCode()+"(" +rawResponse.getStatusLine().getReasonPhrase()+")");
			listener.getLogger().println("Response Body: " + EntityUtils.toString(rawResponse.getEntity()));
			throw new RuntimeException("Failed to set build status!");
		}else{
			listener.getLogger().println("Updated state to "+step.getState());
		}

		return null;
	}

}
