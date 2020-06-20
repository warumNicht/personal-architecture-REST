package architecture.unit.services;

import architecture.repositories.ImageRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ImageServiceUnitTests {
    @MockBean
    private ImageRepository imageRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Test
    public void t() {
        System.out.println();
        System.out.println();
    }
}
