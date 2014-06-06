package com.jdml.cucumber.js.execution;

import java.util.*;

public final class CucumberRunSettings
{
    private String _featurePath;
    private String _cucumberPath;
    private String _arguments;
    private int _debugPort = -1;
    private List<String> _tags;

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

    public List<String> getTags() { return _tags; }

    public int getDebugPort() {
        return _debugPort;
    }

    public static class Builder {
        private String _featurePath;
        private String _cucumberPath;
        private String _arguments;
        private int _debugPort = -1;
        private List<String> _tags;

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

        public Builder setTags(String tags) {
            setTags(tags != null ? Arrays.asList(tags.split(" ")) : null);
            return this;
        }

        private void setTags(List<String> tags) {
            if (tags == null) {
                _tags = null;
            } else {
                _tags = new ArrayList<String>();
                for (int i=0; i<tags.size(); i++) {
                    String tag = tags.get(i);
                    if (tag != null) {
                        tag = tag.trim();
                        if (!tag.isEmpty()) {
                            _tags.add(tag);
                        }
                    }
                }

                if (_tags.size() == 0) {
                    _tags = null;
                }
            }
        }

        public CucumberRunSettings build() {
            CucumberRunSettings result = new CucumberRunSettings();
            result._cucumberPath = _cucumberPath != null ? _cucumberPath : "";
            result._featurePath = _featurePath != null ? _featurePath : "";
            result._arguments = _arguments != null ? _arguments : "";
            result._tags = Collections.unmodifiableList(_tags != null ? _tags : new ArrayList<String>());
            result._debugPort = _debugPort;
            return result;
        }
    }
}