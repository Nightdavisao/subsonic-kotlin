#!/bin/bash
set -e

MODULES=(subsonic-client subsonic-api)
SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
MAVEN_REPO_GIT_REPO="https://github.com/Nightdavisao/maven-repo.git"
MAVEN_REPO_DIR="/tmp/maven-repo"

echo "Cloning the maven 'repository'..."
if [[ -n "$MAVEN_REPO_DIR" ]]; then
    rm -rf "$MAVEN_REPO_DIR"
fi

git clone --depth=1 "$MAVEN_REPO_GIT_REPO" "$MAVEN_REPO_DIR"

if [[ -n "${GH_TOKEN}" ]]; then
    echo "GH_TOKEN is set; setting appropriate git config for workflow"
    git config --local user.name 'github-actions[bot]'
    git config --local user.email 'github-actions[bot]@users.noreply.github.com'
    # https://stackoverflow.com/a/57229018
    git config --local url."https://api:$GH_TOKEN@github.com/".insteadOf "https://github.com/"
    git config --local url."https://ssh:$GH_TOKEN@github.com/".insteadOf "ssh://git@github.com/"
    git config --local url."https://git:$GH_TOKEN@github.com/".insteadOf "git@github.com:"
fi

echo "Building and publishing..."
cd "$SCRIPT_DIR"
./gradlew publishAllPublicationsToGithubRepository --stacktrace

echo "Copying all artifacts to maven-repo..."
for module in "${MODULES[@]}"; do
    echo "Copying $module -> maven-repo..."
    cp -r "$SCRIPT_DIR/$module/build/maven-repo/"* "$MAVEN_REPO_DIR/"
done

echo "Pushing to Git..."
cd "$MAVEN_REPO_DIR"
git add -A
git commit -m "publish: $MODULE_NAME $(grep '^version' \
    "$SCRIPT_DIR/$MODULE_NAME/build.gradle.kts" | head -1 | \
    sed 's/.*"\(.*\)".*/\1/')"
git push

echo "Done!"