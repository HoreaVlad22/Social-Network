package repository;

import domain.entities.Entity;
import domain.exceptions.RepositoryException;
import domain.validator.Validator;
import utils.StreamUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Abstract class for file-based repository operations using the Template Method pattern
 * and Java NIO / Stream API for file I/O.
 * @param <ID> the type of the identifier
 * @param <EType> the type of the entity
 */
public abstract class AbstractFileRepository<ID, EType extends Entity<ID>> extends InMemoryRepository<ID, EType> {
    private final Path path;

    /**
     * Creates a file repository for the specified file path and validator.
     * @param fileName path to the backing text file
     * @param validator validator for entities
     */
    public AbstractFileRepository(String fileName, Validator<ID, EType> validator) {
        super(validator);
        this.path = Paths.get(fileName);
    }

    protected abstract EType extractEntity(List<String> attributes);
    protected abstract String createEntityAsString(EType entity);

    /**
     * Loads data from the file into memory using {@code Files.lines} and {@code IntStream.range}.
     * Corrupted lines are skipped and reported together with their 1-based line number.
     * @throws RepositoryException if an I/O error occurs while reading the file
     */
    protected void loadData() {
        List<String> lines;
        try (Stream<String> lineStream = Files.lines(path)) {
            lines = lineStream.toList();
        } catch (NoSuchFileException e) {
            return; // File does not exist yet; it will be created on first write
        } catch (IOException e) {
            throw new RepositoryException("Error reading from file!", e);
        }

        IntStream.range(0, lines.size()).forEach(index -> {
            String line = lines.get(index).trim();
            if (line.isEmpty()) return;
            try {
                List<String> attributes = Arrays.asList(line.split(";", -1));
                super.add(extractEntity(attributes));
            } catch (Exception e) {
                System.err.println("Line " + (index + 1) + " parsing error: " + e.getMessage());
            }
        });
    }

    /**
     * Writes all entities currently in memory to the backing file using {@code Files.write}.
     * @throws RepositoryException if an I/O error occurs while writing to the file
     */
    private void writeToFile() {
        List<String> lines = StreamUtils.toStream(super.findAll())
                .map(this::createEntityAsString)
                .collect(Collectors.toList());
        try {
            Files.write(path, lines);
        } catch (IOException e) {
            throw new RepositoryException("Error writing to file!", e);
        }
    }

    @Override
    public Optional<EType> add(EType entity) {
        Optional<EType> result = super.add(entity);
        if (result.isEmpty()) writeToFile();
        return result;
    }

    @Override
    public Optional<EType> save(EType entity) {
        Optional<EType> result = super.save(entity);
        if (result.isEmpty()) writeToFile();
        return result;
    }

    @Override
    public Optional<EType> delete(ID id) {
        Optional<EType> result = super.delete(id);
        if (result.isPresent()) writeToFile();
        return result;
    }

    @Override
    public Optional<EType> update(EType entity) {
        Optional<EType> result = super.update(entity);
        if (result.isEmpty()) writeToFile();
        return result;
    }
}