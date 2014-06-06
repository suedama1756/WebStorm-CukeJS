package com.jdml.cucumber.js.execution;

import java.util.*;

public final class CucumberRunSettings
{
    private static final ArrayList<String> EMPTY_TAGS = new ArrayList<String>();
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
            _tags = tags != null ? normalizeTags(tags) : null;
            return this;
        }

        private ArrayList<String> normalizeTags(String tags) {
            if (tags == null) {
                return null;
            }

            //  normalize whitespace
            tags = tags.replaceAll(",\\s+", ",").replaceAll("\\s+", " ");

            //  allocate result
            ArrayList<String> result = new ArrayList<String>();

            //  add to list in groups, e.g. "a,b c" will be added as ["a,b", "c"]
            String[] tagGroups = tags.split(" ");
            for (int i=0; i<tagGroups.length; i++) {
                String[] tagGroup = tagGroups[i].split(",");

                //  normalize tag group
                StringBuffer output = new StringBuffer();
                for (int j=0; j<tagGroup.length; j++) {
                    String tag = normalizeTag(tagGroup[j]);
                    if (tag != null) {
                        if (output.length() > 0) {
                            output.append(',');
                        }
                        output.append(tag);
                    }
                }

                //  add tag group
                if (output.length() > 0) {
                    result.add(output.toString());
                }
            }

            return result;
        }

        private String normalizeTag(String tag) {
            if (tag.charAt(0) == '~') {
                if (tag.length() == 1) {
                    return null;
                }
                if (tag.charAt(1) != '@') {
                    return "~@" + tag.substring(1);
                }
            } else if (tag.charAt(0) != '@') {
                return "@" + tag;
            }
            return tag;
        }

        public CucumberRunSettings build() {
            CucumberRunSettings result = new CucumberRunSettings();
            result._cucumberPath = _cucumberPath != null ? _cucumberPath : "";
            result._featurePath = _featurePath != null ? _featurePath : "";
            result._arguments = _arguments != null ? _arguments : "";
            result._tags = Collections.unmodifiableList(_tags != null ? _tags : EMPTY_TAGS);
            result._debugPort = _debugPort;
            return result;
        }
    }
}