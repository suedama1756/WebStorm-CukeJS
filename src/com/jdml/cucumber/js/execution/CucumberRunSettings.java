package com.jdml.cucumber.js.execution;

public final class CucumberRunSettings
{
    private String _featurePath;
    private String _cucumberPath;
    private String _arguments;
    private int _debugPort = -1;

    private CucumberRunSettings() {
    }

    public String getFeaturePath() {
        return _featurePath;
    }

    public String getCucumberPath() {
        return _cucumberPath;
    }

    public String getArguments() {
        return _arguments;
    }

    public int getDebugPort() {
        return _debugPort;
    }

    public static class Builder {
        private String _featurePath;
        private String _cucumberPath;
        private String _arguments;
        private int _debugPort = -1;

        public Builder setFeaturePath(String featurePath) {
            _featurePath = featurePath;
            return this;
        }

        public Builder setCucumberPath(String cucumberPath) {
            _cucumberPath = cucumberPath;
            return this;
        }

        public Builder setDebugPort(int debugPort) {
            if (_debugPort < -1) {
                _debugPort = -1;
            }
            _debugPort = debugPort;
            return this;
        }

        public Builder setArguments(String arguments) {
            _arguments = arguments;
            return this;
        }

        public CucumberRunSettings build() {
            CucumberRunSettings result = new CucumberRunSettings();
            result._cucumberPath = _cucumberPath != null ? _cucumberPath : "";
            result._featurePath = _featurePath != null ? _featurePath : "";
            result._arguments = _arguments != null ? _arguments : "";
            result._debugPort = _debugPort;
            return result;
        }
    }
}