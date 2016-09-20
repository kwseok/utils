const {ButtonToolbar, Button} = ReactBootstrap;

export const buttonsInstance = (
    <ButtonToolbar>
        <Button href='#'>Link</Button>
        <Button>Button</Button>
    </ButtonToolbar>
);

React.render(buttonsInstance, document.getElementById("app"));