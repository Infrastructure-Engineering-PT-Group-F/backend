module.exports = {
  extends: ["@commitlint/config-conventional"],
  rules: {
    "references-empty": [2, "never"],
  },
  // release-please's generated release commit has no issue reference by design
  // matches `pull-request-title-pattern` in release-please-config.json.
  ignores: [(message) => /^chore\(release\): release /.test(message)],
};
