package games.merge;

public record ItemNode(String family, int level) {
    public ItemNode {
        if (family == null || family.isBlank()) {
            throw new IllegalArgumentException("family must not be blank");
        }
        if (level < 1) {
            throw new IllegalArgumentException("level must be positive");
        }
    }

    public boolean canMerge(ItemNode other) {
        return other != null && family.equals(other.family) && level == other.level;
    }

    public ItemNode merge(ItemNode other) {
        if (!canMerge(other)) {
            throw new IllegalArgumentException("items do not match");
        }
        return new ItemNode(family, level + 1);
    }
}
