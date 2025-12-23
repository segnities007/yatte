#!/bin/sh

# Copy hooks to .git/hooks
cp scripts/pre-commit .git/hooks/pre-commit
cp scripts/pre-push .git/hooks/pre-push

# Make them executable
chmod +x .git/hooks/pre-commit
chmod +x .git/hooks/pre-push

echo "Git hooks installed successfully!"
