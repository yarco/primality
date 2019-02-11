package com.example.primecheck.message;

public class PrimalityResult {

	private String input;
	private String status;


	public PrimalityResult(String input, String status) {
		this.input = input;
		this.status = status;
	}

	public PrimalityResult() {
		this.input = "unknown";
		this.status = "unknown";
	}

	public String getInput() {
		return input;
	}

	public void setInput(String input) {
		this.input = input;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
