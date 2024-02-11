package com.smosky.boilerplateserver.database;

import com.smosky.boilerplateserver.spring.Dependency;
import com.smosky.boilerplateserver.spring.DependencyType;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class DataStorage {
  private List<DependencyType> dependencyTypes= new ArrayList<>();
  private List<Dependency> dependencies= new ArrayList<>();

  public void pushDependencyTypeToList(DependencyType dependencyType) {
    dependencyTypes.add(dependencyType);
  }

  public void pushDependencyToList(Dependency dependency) {
    dependencies.add(dependency);
  }


}
