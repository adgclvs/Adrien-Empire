package adrien;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import adrien.buildings.BuildingsManager.Building;
import adrien.buildings.BuildingsManager.BuildingType;
import adrien.buildings.BuildingsManager.Position;
import adrien.game.Inhabitants;
import adrien.resources.Resource;
import adrien.resources.ResourceRequirement;
import adrien.resources.ResourceType;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BuildingTest {

    private Building building;
    private Resource mockResource;
    private Inhabitants mockInhabitants;

    @BeforeEach
    void setUp() {
        // Créer des mocks pour Resource et Inhabitants
        mockResource = mock(Resource.class);
        mockInhabitants = mock(Inhabitants.class);

        // Configurer les mocks pour retourner des valeurs spécifiques
        when(mockResource.getResource(ResourceType.WOOD)).thenReturn(100);
        when(mockResource.getResource(ResourceType.FOOD)).thenReturn(100);
        when(mockResource.getResource(ResourceType.STONE)).thenReturn(100);

        when(mockInhabitants.getNumberInhabitants()).thenReturn(10);
        when(mockInhabitants.getNumberWorkers()).thenReturn(0);

        // Initialise un bâtiment avec des valeurs fictives
        Position position = new Position(0, 0);
        ResourceRequirement[] constructionMaterials = { new ResourceRequirement(ResourceType.WOOD, 10) };
        ResourceRequirement[] consumption = { new ResourceRequirement(ResourceType.FOOD, 5) };
        ResourceRequirement[] production = { new ResourceRequirement(ResourceType.STONE, 2) };
        building = new Building(position, BuildingType.HOUSE, 4, 2, 2, 2, 2, constructionMaterials, consumption, production) {};

        // Injecter les mocks dans les instances singleton
        Resource.setInstance(mockResource);
        Inhabitants.setInstance(mockInhabitants);
    }

    @Test
    void testAddWorkersSuccess() {
        when(mockInhabitants.addWorkers(1)).thenReturn(true);

        boolean result = building.addWorkers(1);

        assertTrue(result);
        assertEquals(1, building.getCurrentWorkers());
        assertTrue(building.isProducing());
    }

    @Test
    void testAddWorkersFail() {
        when(mockInhabitants.addWorkers(1)).thenReturn(false);

        boolean result = building.addWorkers(1);

        assertFalse(result);
        assertEquals(0, building.getCurrentWorkers());
        assertFalse(building.isProducing());
    }

    @Test
    void testRemoveWorkersSuccess() {
        building.addWorkers(1);
        when(mockInhabitants.removeWorkers(1)).thenReturn(true);

        boolean result = building.removeWorkers(1);

        assertTrue(result);
        assertEquals(0, building.getCurrentWorkers());
        assertFalse(building.isProducing());
    }

    @Test
    void testRemoveWorkersFail() {
        building.addWorkers(1);
        when(mockInhabitants.removeWorkers(1)).thenReturn(false);

        boolean result = building.removeWorkers(1);

        assertFalse(result);
        assertEquals(1, building.getCurrentWorkers());
        assertTrue(building.isProducing());
    }

    @Test
    void testProduceResources() {
        building.addWorkers(1);
        building.produceResources();

        ArgumentCaptor<ResourceRequirement> argumentCaptor = ArgumentCaptor.forClass(ResourceRequirement.class);
        verify(mockResource).addResource(argumentCaptor.capture());
        ResourceRequirement capturedArgument = argumentCaptor.getValue();

        assertEquals(ResourceType.STONE, capturedArgument.getResourceType());
        assertEquals(2, capturedArgument.getQuantity());
    }

    @Test
    void testConsumeResourcesSuccess() {
        building.addWorkers(1);
        when(mockResource.haveAllResources(any())).thenReturn(true);

        building.consumeResources();

        ArgumentCaptor<ResourceRequirement> argumentCaptor = ArgumentCaptor.forClass(ResourceRequirement.class);
        verify(mockResource).consumeResource(argumentCaptor.capture());
        ResourceRequirement capturedArgument = argumentCaptor.getValue();

        assertEquals(ResourceType.FOOD, capturedArgument.getResourceType());
        assertEquals(5, capturedArgument.getQuantity());
    }

    @Test
    void testConsumeResourcesFail() {
        building.addWorkers(1);
        when(mockResource.haveAllResources(any())).thenReturn(false);

        building.consumeResources();

        verify(mockResource, never()).consumeResource(any());
        assertFalse(building.isProducing());
    }

    @Test
    void testCostBuildingResourcesSuccess() {
        when(mockResource.consumeResource(any())).thenReturn(true);

        boolean result = building.costBuildingResources();

        assertTrue(result);
    }

    @Test
    void testCostBuildingResourcesFail() {
        when(mockResource.consumeResource(any())).thenReturn(false);

        boolean result = building.costBuildingResources();

        assertFalse(result);
    }
}