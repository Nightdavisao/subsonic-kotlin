#!/bin/bash
set -e

PROJECT_GROUP=$(sed -n 's/.*group = "\(.*\)".*/\1/p' build.gradle.kts)
PROJECT_VERSION=$(sed -n 's/.*version = "\(.*\)".*/\1/p' build.gradle.kts)

MODULES=(subsonic-client subsonic-api)
SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
MAVEN_REPO_GIT_REPO="https://github.com/Nightdavisao/maven-repo.git"
MAVEN_REPO_DIR="/tmp/maven-repo"

echo "Cloning the maven 'repository'..."
if [[ -n "$MAVEN_REPO_DIR" ]]; then
    rm -rf "$MAVEN_REPO_DIR"
fi

git clone --depth=1 "$MAVEN_REPO_GIT_REPO" "$MAVEN_REPO_DIR"

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

if [[ -n "${GH_TOKEN}" ]]; then
    # does --local even work here? i don't fucking know
    echo "GH_TOKEN is set; setting appropriate git config for workflow"
    cmd_prefix="git config --local"

    $cmd_prefix user.name 'github-actions[bot]'
    $cmd_prefix user.email 'github-actions[bot]@users.noreply.github.com'
    # https://stackoverflow.com/a/57229018
    $cmd_prefix url."https://api:$GH_TOKEN@github.com/".insteadOf "https://github.com/"
    $cmd_prefix url."https://ssh:$GH_TOKEN@github.com/".insteadOf "ssh://git@github.com/"
    $cmd_prefix url."https://git:$GH_TOKEN@github.com/".insteadOf "git@github.com:"
fi

git add -A
git commit -m "publish: $PROJECT_GROUP ($PROJECT_VERSION)"
git push --force-with-lease

echo "Done!"