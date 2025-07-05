import functools
import hashlib
import os
import re
from pathlib import Path

VERSIONS_PATH = Path("versions")
MOD_JAR_DIR = Path("mod-jars")

# e.g. unlimitedtrade-v2.0.0-mc1.21.3+build.72.jar → "1.21.3"
FILE_VERSION_COMPILER_PATTERN = re.compile(r"-mc(\d+\.\d+\.\d+)(?:\+build\.\d+)?$")
GRADLE_GAME_VERSION_COMPILER_PATTERN = re.compile(r"^game_versions=(.*)$", re.MULTILINE)


def get_sha256_hash(file_path: Path) -> str:
    sha256_hash = hashlib.sha256()
    with file_path.open("rb") as f:
        for buf in iter(functools.partial(f.read, 4096), b""):
            sha256_hash.update(buf)
    return sha256_hash.hexdigest()


def human_readable_size(size_bytes: float) -> str:
    for unit in " KMGT":
        if size_bytes < 1024:
            return f"{size_bytes:.1f} {unit.strip()}B"
        size_bytes /= 1024
    return f"{size_bytes:.1f} PB"


def main() -> None:
    lines = [
        "## Summary:",
        "",
        "| For Minecraft | File | Size | SHA256 |",
        "| :-----------: | :--: | :--: | :----: |",
    ]

    for path in MOD_JAR_DIR.glob("*.jar"):
        filename = path.name
        stem = path.stem
        file_hash = get_sha256_hash(path)

        match = FILE_VERSION_COMPILER_PATTERN.search(stem)
        if not match:
            print(f"⚠️ Unknown file name: {filename}")
            continue

        mc_dir_version = match.group(1)
        prop_path = VERSIONS_PATH / mc_dir_version / "gradle.properties"

        if not prop_path.exists():
            print(f"⚠️ Missing gradle.properties: {prop_path}")
            continue

        content = prop_path.read_text(encoding="utf-8")
        version_match = GRADLE_GAME_VERSION_COMPILER_PATTERN.search(content)
        if not version_match:
            print(f"⚠️ Missing 'game_versions=' in: {prop_path}")
            continue

        file_size = human_readable_size(path.stat().st_size)
        game_versions = ", ".join(f"`{name.strip()}`" for name in version_match.group(1).strip().split("\\n"))
        lines.append(f"| {game_versions} | `{filename}` | `{file_size}` | `{file_hash}` |")

    lines.append("")

    Path(os.environ.get("GITHUB_STEP_SUMMARY", "summary.md")).write_text("\n".join(lines), encoding="utf-8")


if __name__ == "__main__":
    main()
