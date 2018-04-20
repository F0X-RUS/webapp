package ru.sgmu.seem.webapp.domains;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

@Entity
@AttributeOverride(name = "imageName", column = @Column(name = "image_name", insertable = false, updatable = false))
public class Post extends EntityDetails implements Comparable<Post>{

    @Size(min = 5)
    @Column(columnDefinition = "text")
    private String content;

    @ManyToOne
    @JoinColumn(name = "thread_id", nullable = false)
    private Thread thread;

    @OneToMany(mappedBy = "thread", cascade = CascadeType.ALL)
    @OrderBy("origin_name")
    private SortedSet<File> files = new TreeSet<>();

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Thread getThread() {
        return thread;
    }

    public void setThread(Thread thread) {
        this.thread = thread;
    }

    public SortedSet<File> getFiles() {
        return files;
    }

    public void setFiles(SortedSet<File> files) {
        this.files = files;
    }

    @Override
    public int compareTo(Post o) {
        return o.getId().compareTo(o.getId());
    }
}
