const {ButtonGroup, Button} = ReactBootstrap;

export const buttonGroupInstance = (
    <ButtonGroup>
        <Button>Left</Button>
        <Button>Middle</Button>
        <Button>Right</Button>
    </ButtonGroup>
);

React.render(buttonGroupInstance, document.getElementById("app"));