#!/bin/bash
set -e

PROJECT_GROUP=$(sed -n 's/.*group = "\(.*\)".*/\1/p' build.gradle.kts)
PROJECT_VERSION=$(sed -n 's/.*version = "\(.*\)".*/\1/p' build.gradle.kts)

MODULES=(subsonic-client subsonic-api)
SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"

echo "Cloning the maven 'repository'..."
if [[ -n "$MAVEN_REPO_DIR" ]]; then
    rm -rf "$MAVEN_REPO_DIR"
fi

git clone --depth=1 "$MAVEN_REPO_GIT_REPO" "$MAVEN_REPO_DIR"

echo "Building and publishing..."
cd "$SCRIPT_DIR"
./gradlew publishAllPublicationsToGithubRepository --stacktrace

if [[ -n "$GH_TOKEN" ]]; then
    echo "Copying all artifacts to maven-repo..."
    for module in "${MODULES[@]}"; do
        echo "Copying $module -> maven-repo..."
        cp -r "$SCRIPT_DIR/$module/build/maven-repo/"* "$MAVEN_REPO_DIR/"
    done

    MAVEN_REPO_GIT_REPO="https://x-access-token:$GH_TOKEN@github.com/Nightdavisao/maven-repo.git"
    MAVEN_REPO_DIR="/tmp/maven-repo"

    echo "Pushing to Git..."
    cd "$MAVEN_REPO_DIR"

    # does --local even work here? i don't fucking know
    echo "GH_TOKEN is set; setting appropriate git config for workflow"

    git config --global user.name 'github-actions[bot]'
    git config --global user.email 'github-actions[bot]@users.noreply.github.com'

    git add -A
    git commit -m "publish: $PROJECT_GROUP ($PROJECT_VERSION)"
    git push --force-with-lease
    echo "Pushed publish commit"
else
    echo "GH_TOKEN not found. Sorry."
fi

echo "Done!"